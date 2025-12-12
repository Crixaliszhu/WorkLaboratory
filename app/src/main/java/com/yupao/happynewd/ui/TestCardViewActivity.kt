package com.yupao.happynewd.ui

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.yupao.happynewd.R
import com.yupao.happynewd.adapter.CardPageAdapter
import com.yupao.happynewd.model.CmsWechatItemEntity

class TestCardViewActivity : AppCompatActivity() {

    private var viewPager: ViewPager2? = null
    private var seekBar: SeekBar? = null
    private val adapter by lazy {
        CardPageAdapter(
            listOf(
                CmsWechatItemEntity(
                    type = 0,
                    title = "加群",
                    content = "加群加群加群加群",
                    switchContent = listOf("11111111111111111111111", "222222222222222222222")
                ), CmsWechatItemEntity(
                    type = 1,
                    title = "公众号",
                    content = "公众号公众号公众号公众号",
                    switchContent = listOf("333333333333333333", "4444444444444444")
                )
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_test)
        viewPager = findViewById(R.id.viewPager2)
        seekBar = findViewById(R.id.sbSeekBar)
        viewPager?.adapter = adapter
        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val itemWidth = viewPager?.getChildAt(0)?.width ?: 0
                val maxScroll = ((adapter.itemCount - 1) * itemWidth)
                val progress = itemWidth * position + positionOffsetPixels
                seekBar?.max = maxScroll
                seekBar?.progress = maxScroll - progress
            }

        })
    }
}