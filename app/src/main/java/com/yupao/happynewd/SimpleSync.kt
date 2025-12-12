package com.yupao.happynewd

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.*

class SimpleSync {
    /**
     * 开启100个协程；让他们重复执行1000次 action任务
     * @param action SuspendFunction0<Unit>
     */
    private suspend fun massiveRun(action: suspend () -> Unit) {
        val n = 100  // number of coroutines to launch
        val k = 1000 // times an action is repeated by each coroutine
        val time = measureTimeMillis {
            coroutineScope { // scope for coroutines
                repeat(n) {
                    launch {
                        repeat(k) { action() }
                    }
                }
            }
        }
        println("Completed ${n * k} actions in $time ms")
    }
    /**
     * 原执行时间 30~40ms
     */

//    @Volatile // volatile 只能无法保证原子性，所以不能解决问题

    /**
     * AtomicInteger(0) 最快的解决方案，适合普通计数器，队列，集合和其他标准数据结构和他们的基本操作；
     * 但不适合复杂的数据结构，不能解决现成的安全实现的复杂操作
     * 执行时间 14~19ms
     */

    /**
     * 以细粒度限制线程,每个增量操作都通过withContext(countContext)块从多线程Dispatchers.Default上下文切换到单线程上下文； 执行时间大于1000ms
     * withContext(Dispatchers.Default) {
     *     massiveRun {
     *        withContext(countContext){
     *            counter++
     *        }
     *     }
     * }
     */
    val countContext = newSingleThreadContext("countContext")

    /**
     * 以粗力度限制线程-一切都限制在单线程上下文，在单线程中运行所有协程 执行时间25~35ms
     *  withContext(countContext) {
     *     massiveRun {
     *        counter++
     *     }
     *  }
     */
    private var counter = 0

    //互斥-使用永远不会同时执行的 关键代码块来保护共享状态的所有修改，即加锁Synchronous，ReentrantLock,在协程中的替代是Mutex
    //mutex.lock(),mutex.unlock(),扩展函数Mutex.withLock{}更方便的替代: mutex.lock(); try{...}finally{mutex.unlock()}
    //执行时间 450ms~1100ms
    private val mutex = Mutex()

    fun main() = runBlocking {
        counter = 0
        withContext(Dispatchers.Default) {
            massiveRun {
                mutex.withLock {
                    counter++
                }
            }
        }
        println("Counter = $counter")
    }

    // 这个函数启动一个新的计数器 actor
    private fun CoroutineScope.counterActor() = actor<CounterMsg> {
        var counter = 0 // actor 状态
        for (msg in channel) { // 即将到来消息的迭代器
            when (msg) {
                is IncCounter -> counter++
                is GetCounter -> msg.response.complete(counter)
            }
        }
    }

    fun actorMain() = runBlocking<Unit> {
        val counter = counterActor() // 创建该 actor
        withContext(Dispatchers.Default) {
            massiveRun {
                counter.send(IncCounter)
            }
        }
        // 发送一条消息以用来从一个 actor 中获取计数值
        val response = CompletableDeferred<Int>()
        counter.send(GetCounter(response))
        println("Counter = ${response.await()}")
        counter.close() // 关闭该actor
    }
}

// 计数器 Actor 的各种类型
sealed class CounterMsg
object IncCounter : CounterMsg() // 递增计数器的单向消息
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // 携带回复的请求

fun String?.toIntOrZero(): Int = if (this == null) 0 else this.toIntOrNull() ?: 0