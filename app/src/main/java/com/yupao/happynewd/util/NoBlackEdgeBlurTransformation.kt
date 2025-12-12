package com.yupao.happynewd.util

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * 无黑边模糊变换
 * 用于Glide图片加载时的模糊处理，解决黑边问题
 */
class NoBlackEdgeBlurTransformation @JvmOverloads constructor(
    private val radius: Int = DEFAULT_RADIUS
) : BitmapTransformation() {

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val clampedRadius = radius.coerceIn(1, MAX_RADIUS)
        return NoBlackEdgeBlurUtil.blurWithoutBlackEdge(toTransform, clampedRadius)
    }

    override fun toString(): String {
        return "NoBlackEdgeBlurTransformation(radius=$radius)"
    }

    override fun equals(other: Any?): Boolean {
        return other is NoBlackEdgeBlurTransformation && other.radius == radius
    }

    override fun hashCode(): Int {
        return ID.hashCode() + radius * 1000
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + radius).toByteArray(CHARSET))
    }

    companion object {
        private const val VERSION = 1
        private const val ID = "NoBlackEdgeBlurTransformation.$VERSION"
        private const val MAX_RADIUS = 25
        private const val DEFAULT_RADIUS = 10
    }
}