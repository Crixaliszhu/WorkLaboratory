package com.yupao.happynewd.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.yupao.happynewd.R

/**
 * 金币奖励组件
 * 支持左右两个金币图片的旋转和动态数字显示
 */
class CoinRewardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var leftCoinContainer: FrameLayout
    private lateinit var rightCoinContainer: FrameLayout
    private lateinit var leftCoinImage: ImageView
    private lateinit var rightCoinImage: ImageView
    private lateinit var leftCoinText: TextView
    private lateinit var rightCoinText: TextView
    private lateinit var leftLabel: TextView
    private lateinit var rightLabel: TextView

    // 默认旋转角度
    private var leftRotationAngle = -15f
    private var rightRotationAngle = 15f

    init {
        initView()
        setupDefaultValues()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.view_coin_reward, this, true)
        
        leftCoinContainer = findViewById(R.id.leftCoinContainer)
        rightCoinContainer = findViewById(R.id.rightCoinContainer)
        leftCoinImage = findViewById(R.id.leftCoinImage)
        rightCoinImage = findViewById(R.id.rightCoinImage)
        leftCoinText = findViewById(R.id.leftCoinText)
        rightCoinText = findViewById(R.id.rightCoinText)
        leftLabel = findViewById(R.id.leftLabel)
        rightLabel = findViewById(R.id.rightLabel)
    }

    private fun setupDefaultValues() {
        // 设置默认旋转角度
        applyRotation()
        
        // 设置默认值
        setLeftCoinValue(300)
        setRightCoinValue(400)
        setLeftLabel("直接领取")
        setRightLabel("看视频领取")
    }

    /**
     * 应用旋转效果
     */
    private fun applyRotation() {
        // 左边容器整体旋转
        leftCoinContainer.rotation = leftRotationAngle
        
        // 右边容器整体旋转
        rightCoinContainer.rotation = rightRotationAngle
    }

    /**
     * 设置左边金币数值
     */
    fun setLeftCoinValue(value: Int) {
        leftCoinText.text = value.toString()
    }

    /**
     * 设置右边金币数值
     */
    fun setRightCoinValue(value: Int) {
        rightCoinText.text = value.toString()
    }

    /**
     * 设置左边标签文本
     */
    fun setLeftLabel(text: String) {
        leftLabel.text = text
    }

    /**
     * 设置右边标签文本
     */
    fun setRightLabel(text: String) {
        rightLabel.text = text
    }

    /**
     * 设置左边旋转角度
     */
    fun setLeftRotationAngle(angle: Float) {
        leftRotationAngle = angle
        leftCoinContainer.rotation = leftRotationAngle
    }

    /**
     * 设置右边旋转角度
     */
    fun setRightRotationAngle(angle: Float) {
        rightRotationAngle = angle
        rightCoinContainer.rotation = rightRotationAngle
    }

    /**
     * 设置金币图片资源
     */
    fun setCoinImageResource(resId: Int) {
        leftCoinImage.setImageResource(resId)
        rightCoinImage.setImageResource(resId)
    }

    /**
     * 设置左边金币点击监听
     */
    fun setOnLeftCoinClickListener(listener: OnClickListener?) {
        leftCoinContainer.setOnClickListener(listener)
    }

    /**
     * 设置右边金币点击监听
     */
    fun setOnRightCoinClickListener(listener: OnClickListener?) {
        rightCoinContainer.setOnClickListener(listener)
    }

    /**
     * 播放入场动画
     */
    fun playEnterAnimation() {
        // 左边金币从左侧飞入
        leftCoinContainer.apply {
            translationX = -300f
            alpha = 0f
            animate()
                .translationX(0f)
                .alpha(1f)
                .setDuration(600)
                .setStartDelay(0)
                .start()
        }

        // 右边金币从右侧飞入
        rightCoinContainer.apply {
            translationX = 300f
            alpha = 0f
            animate()
                .translationX(0f)
                .alpha(1f)
                .setDuration(600)
                .setStartDelay(200)
                .start()
        }
    }

    /**
     * 播放点击动画
     */
    fun playClickAnimation(isLeft: Boolean) {
        val targetView = if (isLeft) leftCoinContainer else rightCoinContainer
        
        targetView.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(150)
            .withEndAction {
                targetView.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
                    .start()
            }
            .start()
    }
}
