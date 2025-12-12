package com.yupao.happynewd

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Arrays
import java.util.Collections

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        val list = listOf(
            listOf(1, 4).toIntArray(),
            listOf(2, 3).toIntArray(),
            listOf(8, 10).toIntArray(),
        )
        val has = LeetCodeUtils.mergeIntervals(list.toTypedArray())
        println("-----------------")
        has.forEach {
            println(Arrays.toString(it))
        }

//        val mBrowse = listOf("kk", "ff", "dd", "jj", "11", "22", "33", "cc")
//        println(mBrowse)
//        println(mBrowse.toMutableList())
//        val newBrowse = mutableListOf("cc", "aa", "ff", "dd", "mm", "jj")
//        println("----------------")
//        println(mBrowse)
    }
}