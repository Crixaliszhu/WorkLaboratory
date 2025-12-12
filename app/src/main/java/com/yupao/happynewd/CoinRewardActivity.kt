package com.yupao.happynewd

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yupao.happynewd.view.CoinRewardView

/**
 * 金币奖励演示Activity
 */
class CoinRewardActivity : AppCompatActivity() {

    private lateinit var coinRewardView: CoinRewardView
    private lateinit var btnPlayAnimation: Button
    private lateinit var btnChangeValues: Button
    private lateinit var btnChangeRotation: Button

    private var currentLeftValue = 300
    private var currentRightValue = 400

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_reward)

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        coinRewardView = findViewById(R.id.coinRewardView)
        btnPlayAnimation = findViewById(R.id.btnPlayAnimation)
        btnChangeValues = findViewById(R.id.btnChangeValues)
        btnChangeRotation = findViewById(R.id.btnChangeRotation)

        // 设置金币图片（如果有金币图片资源的话）
        // coinRewardView.setCoinImageResource(R.drawable.ic_coin)
    }

    private fun setupClickListeners() {
        // 左边金币点击事件
        coinRewardView.setOnLeftCoinClickListener {
            Toast.makeText(this, "领取了 $currentLeftValue 金币!", Toast.LENGTH_SHORT).show()
            coinRewardView.playClickAnimation(true)
        }

        // 右边金币点击事件
        coinRewardView.setOnRightCoinClickListener {
            Toast.makeText(this, "观看视频领取了 $currentRightValue 金币!", Toast.LENGTH_SHORT).show()
            coinRewardView.playClickAnimation(false)
        }

        // 播放入场动画
        btnPlayAnimation.setOnClickListener {
            coinRewardView.playEnterAnimation()
        }

        // 改变数值
        btnChangeValues.setOnClickListener {
            currentLeftValue = (200..500).random()
            currentRightValue = (400..800).random()
            
            coinRewardView.setLeftCoinValue(currentLeftValue)
            coinRewardView.setRightCoinValue(currentRightValue)
            
            Toast.makeText(this, "数值已更新", Toast.LENGTH_SHORT).show()
        }

        // 改变旋转角度
        btnChangeRotation.setOnClickListener {
            val leftAngle = (-30..-5).random().toFloat()
            val rightAngle = (5..30).random().toFloat()
            
            coinRewardView.setLeftRotationAngle(leftAngle)
            coinRewardView.setRightRotationAngle(rightAngle)
            
            Toast.makeText(this, "旋转角度已更新: 左${leftAngle}°, 右${rightAngle}°", Toast.LENGTH_SHORT).show()
        }
    }
}
