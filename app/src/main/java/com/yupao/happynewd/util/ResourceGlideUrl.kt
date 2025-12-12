package com.yupao.happynewd.util

import com.bumptech.glide.load.model.GlideUrl

class ResourceGlideUrl(
    private val url: String,
    private val resourceId: String,
    private val width: Int,
) : GlideUrl(url) {

    override fun getCacheKey(): String {
        // 以 resourceId 替代 URL 作为缓存键
        if (resourceId.isBlank()) {
            return url
        }
        return "${resourceId}-${width}"
    }

    override fun toStringUrl(): String {
        // 原始 URL 用于实际的网络请求
        return url
    }
}