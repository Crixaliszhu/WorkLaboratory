package com.yupao.happynewd.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.yupao.happynewd.R


/**
 * 设置性别图标
 * @param gender 性别 1 / 默认-男性 2-女性
 */
@BindingAdapter("genderIcon")
fun ImageView.setGenderIcon(gender: Int?) {
    when (gender) {
        2 -> setImageResource(R.drawable.ic_woman)
        else -> setImageResource(R.drawable.ic_man)
    }
}

/**
 * 设置View的可见性
 * @param visible 是否可见
 */
@BindingAdapter("visible")
fun View.setVisible(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.GONE
}

/**
 * 格式化基本信息文本
 * 格式：城市 · 学历 · 工作年限
 */
@BindingAdapter(
    value = ["hopeCity", "degree", "workAge"],
    requireAll = false
)
fun TextView.formatBaseInfo(
    hopeCity: String?,
    degree: String?,
    workAge: String?
) {
    val parts = mutableListOf<String>()

    if (!hopeCity.isNullOrEmpty()) {
        parts.add(hopeCity)
    }

    if (!degree.isNullOrEmpty()) {
        parts.add(degree)
    }

    if (!workAge.isNullOrEmpty()) {
        parts.add(workAge)
    }

    text = parts.joinToString(" · ")
}

