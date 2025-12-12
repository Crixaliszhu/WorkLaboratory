package com.yupao.happynewd.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.yupao.happynewd.R

class MaxSizeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var maxSizePx: Float = 0f

    init {
        // 读取 XML 中定义的最大尺寸属性
        context.obtainStyledAttributes(attrs, R.styleable.MaxSizeTextView).apply {
            try {
                maxSizePx = getDimension(R.styleable.MaxSizeTextView_maxTextSize, 0f)
            } finally {
                recycle()
            }
        }
    }

    // 动态设置最大尺寸（单位：dp）
    fun setMaxSizeDp(maxDp: Float) {
        maxSizePx = maxDp * resources.displayMetrics.density
        adjustTextSize()
    }

    // 调整当前文字大小
    private fun adjustTextSize() {
        if (maxSizePx > 0 && textSize > maxSizePx) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, maxSizePx)
        }
    }

    // 重写字体设置方法
    override fun setTextSize(unit: Int, size: Float) {
        val pixels = TypedValue.applyDimension(unit, size, resources.displayMetrics)
        val finalSize = if (maxSizePx > 0) pixels.coerceAtMost(maxSizePx) else pixels
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, finalSize)
    }

    // 处理单参数方法（默认单位为 sp）
    override fun setTextSize(size: Float) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }
}