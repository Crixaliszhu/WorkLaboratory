package com.yupao.happynewd.model

import androidx.annotation.Keep

@Keep
data class CmsWechatEntity(
    val list: List<CmsWechatItemEntity>?,
) {
}

@Keep
data class CmsWechatItemEntity(
    val type: Int,
    val title: String?,
    val content: String?,
    val switchContent: List<String>?,
) {

}

