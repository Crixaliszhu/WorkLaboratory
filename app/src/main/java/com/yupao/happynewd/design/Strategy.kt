package com.yupao.happynewd.design

import kotlin.random.Random

/**
 * 优惠计算
 */
interface Strategy {

    fun favorablePrice(price: Double): Double
}


/**
 * 收银员功能
 */
interface Cashier {

    /**
     * 收银员编号
     * @return String
     */
    fun getId(): String

    /**
     * 收银员密码
     * @return String
     */
    fun getPassword(): String
}

/**
 * 商品
 */
interface Goods {

    /**
     * 商品条形码
     * @return String
     */
    fun getGoodsId(): String

    /**
     * 商品价格
     * @return Double
     */
    fun getGoodsPrice(): Double

    /**
     * 商品名
     * @return String
     */
    fun getGoodsName(): String
}

class Tea : Goods {
    override fun getGoodsId(): String {
        return "g1001"
    }

    override fun getGoodsPrice(): Double {
        return 4.5
    }

    override fun getGoodsName(): String {
        return "茶Π"
    }

}

class Noodle : Goods {
    override fun getGoodsId(): String {
        return "g1002"
    }

    override fun getGoodsPrice(): Double {
        return 5.5
    }

    override fun getGoodsName(): String {
        return "康氏菲方便面"
    }

}

class HuoTui : Goods {
    override fun getGoodsId(): String {
        return "g1003"
    }

    override fun getGoodsPrice(): Double {
        return 3.5
    }

    override fun getGoodsName(): String {
        return "金锣火腿肠"
    }

}

class LuEgg : Goods {
    override fun getGoodsId(): String {
        return "g1004"
    }

    override fun getGoodsPrice(): Double {
        return 3.0
    }

    override fun getGoodsName(): String {
        return "金锣火腿肠"
    }

}

/**
 * 计算功能
 */
interface Calculator {
    /**
     * 计算找零
     * @param cash Double
     * @return Double
     */
    fun calculate(cash: Double, price: Double, strategy: Strategy): Double
}

/**
 * 订单
 */
interface Order {

    /**
     * 订单编号
     * @return String
     */
    fun getOrderId(): String

    fun addGoods(good: List<Goods>, price: Double)

    /**
     * 订单价格
     * @return Double
     */
    fun getOrderPrice(): Double

    /**
     * 订单商品详情
     * @return List<Goods>
     */
    fun getOrderGoods(): List<Goods>

    fun getUserId(): String
}

class DefaultOrder(
    private val id: String,
    private val userId: String
) : Order {

    private val goods = mutableListOf<Goods>()
    private var price: Double = 0.0

    companion object {
        /**
         * @param userId 用户id
         * @return Order
         */
        fun createNewOrder(userId: String): Order {
            val random1 = Random.nextInt(10000)
            val random2 = Random.nextInt(10000)
            val random3 = Random.nextInt(10000)
            return DefaultOrder("${random1}${random2}${random3}", userId = userId)
        }
    }

    override fun getOrderId(): String {
        return id
    }

    override fun addGoods(good: List<Goods>, price: Double) {
        goods.addAll(good)
        this.price += price
    }

    override fun getOrderPrice(): Double {
        return price
    }

    override fun getOrderGoods(): List<Goods> {
        return goods
    }

    override fun getUserId(): String {
        return userId
    }

}

/**
 * 管理员系统
 */
interface ManagerSystem {
    /**
     * 添加优惠活动
     * @param strategy Strategy
     */
    fun addStrategy(strategy: Strategy)

    /**
     * 添加收银员
     * @param asst Cashier
     */
    fun addCashier(asst: Cashier)
}

/**
 * 员工系统
 */
interface CashierSystem {

    /**
     * 员工登录系统
     * @param id String
     * @param password String
     */
    fun initSystem(id: String, password: String): Boolean
}

interface PaySystem {

    /**
     * 扫码识别商品，并累加商品总价
     * @param good List<Goods>
     * @return Double
     */
    fun identityGood(good: List<Goods>): Double

    /**
     * 结账
     * @param cash Double 支付金额
     * @return Double 找零
     */
    fun settleAccounts(cash: Double): Double

    /**
     * 开始结账
     */
    fun startCheck(userId: String)
}

/**
 * 佳佳乐超市结账系统
 * @property strategyList MutableList<Strategy>
 * @property orderList MutableList<Order>
 */
class JiaJiaLePaySystem : PaySystem, ManagerSystem {
    private val strategyList = mutableListOf<Strategy>()

    private val orderList = mutableListOf<Order>()

    private val cashierList = mutableListOf<Cashier>()

    //////////////////经理功能/////////////
    override fun addStrategy(strategy: Strategy) {
        strategyList.add(strategy)
        println(strategyList.size)
    }

    override fun addCashier(asst: Cashier) {
        cashierList.add(asst)
    }

    //////////////////员工功能-顾客功能(自助结账)/////////////
    override fun identityGood(good: List<Goods>): Double {
        val order = getOrder() ?: return 0.0
        // 计算本单总金额
        val totalPrice = getGoodsPrice(strategyList, good)
        order.addGoods(good, totalPrice)
        // 显示总金额
        return totalPrice
    }

    override fun settleAccounts(cash: Double): Double {
        val order = getOrder() ?: return cash
        val price = order.getOrderPrice()
        if (cash < price) return cash
        return cash - price
    }

    override fun startCheck(userId: String) {
        val order = DefaultOrder.createNewOrder(userId)
        orderList.add(order)
    }

    /**
     * 获取当前正在结账的订单
     * @return Order?
     */
    private fun getOrder(): Order? {
        if (orderList.isEmpty()) return null
        return orderList[orderList.size - 1]
    }


    /**
     * 计算商品价格
     * @param strategyList List<Strategy>
     * @param good List<Goods>
     * @return Double
     */
    private fun getGoodsPrice(strategyList: List<Strategy>, good: List<Goods>): Double {
        var total = 0.0
        good.forEach { p ->
            total += calculator(p.getGoodsPrice(), strategyList)
        }

        return total
    }

    /**
     * 计算器：暂时不允许叠加使用优惠方案,只能选择最大折扣
     * @param price Double
     * @param strategyList List<Strategy>
     */
    private fun calculator(price: Double, strategyList: List<Strategy>): Double {
        var realPrice = price
        strategyList.forEach {
            val des = it.favorablePrice(price)
            if (realPrice > des) {
                realPrice = des
            }
        }
        return realPrice
    }
}

class EightStrategy : Strategy {
    override fun favorablePrice(price: Double): Double {
        return price * 0.8
    }

}

class Manager(
    private val jiaJiaLePaySystem: JiaJiaLePaySystem
) : ManagerSystem by jiaJiaLePaySystem {
}

class CashierLi(
    private val jiaJiaLePaySystem: JiaJiaLePaySystem
) : Cashier, CashierSystem, PaySystem by jiaJiaLePaySystem{

    override fun getId(): String {
        return "001_li"
    }

    override fun getPassword(): String {
        return "li_abc"
    }

    override fun initSystem(id: String, password: String): Boolean {
        return false
    }

}