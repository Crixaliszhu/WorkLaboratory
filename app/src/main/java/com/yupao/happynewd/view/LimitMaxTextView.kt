package com.yupao.happynewd.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.yupao.happynewd.R
import kotlin.math.min

class LimitMaxTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    // 自定义属性：最大字号(dp)
    private var maxSizeDp: Float = 48f
        set(value) {
            field = value
            maxSizePx = context.resources.displayMetrics.run { value * (densityDpi / 160f) }
            adjustTextSize()
        }

    private var maxSizePx: Float = 0f

    init {
        // 读取XML属性
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.LimitMaxTextView)
            maxSizeDp = typedArray.getFloat(R.styleable.LimitMaxTextView_maxSizeDp, 48f)
            typedArray.recycle()
        }
        maxSizePx = context.resources.displayMetrics.run { maxSizeDp * (densityDpi / 160f) }
        adjustTextSize()
    }

    override fun setTextSize(unit: Int, size: Float) {
        val clampedSize = when (unit) {
            TypedValue.COMPLEX_UNIT_PX -> min(size, maxSizePx)
            TypedValue.COMPLEX_UNIT_SP -> min(size, maxSizeDp)
            TypedValue.COMPLEX_UNIT_DIP -> min(size, maxSizeDp)
            else -> min(size, maxSizePx)
        }
        super.setTextSize(unit, clampedSize)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        adjustTextSize()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        adjustTextSize()
    }

    private fun adjustTextSize() {
        if (maxSizePx <= 0) return
        if(textSize > maxSizePx){
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, maxSizePx)
        }
    }
}