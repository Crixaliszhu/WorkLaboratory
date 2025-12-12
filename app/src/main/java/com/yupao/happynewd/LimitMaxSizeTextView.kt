package com.yupao.happynewd

import android.content.Context
import kotlin.jvm.JvmOverloads
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import android.text.TextUtils
import android.util.TypedValue

/**
 * 限制最大字号
 * @property mRealSize Float 实际字号
 * @constructor
 */
class LimitMaxSizeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var mRealSize = 0f

    override fun setText(text: CharSequence, type: BufferType) {
        if (!TextUtils.equals(text, getText())) {
            super.setText(text, type)
            val fontScale = resources.configuration.fontScale
            if (fontScale >= mLimitSize) {
                if (mRealSize == 0f) {
                    mRealSize = textSize * (mLimitSize / fontScale)
                }
                setTextSize(TypedValue.COMPLEX_UNIT_PX, mRealSize)
            }
        } else {
            super.setText(text, type)
        }
    }

    companion object {
        private const val mLimitSize = 1.2f
    }
}