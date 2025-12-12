package com.yupao.happynewd.util

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * 自定义圆角变换
 * 支持设置圆角半径或圆角百分比
 */
class RoundedCornersTransformation(
    private val radius: Float = 0f,
    private val roundPercent: Float = 0f
) : BitmapTransformation() {

    companion object {
        private const val ID = "com.yupao.happynewd.util.RoundedCornersTransformation"
        private val ID_BYTES = ID.toByteArray(Charsets.UTF_8)
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height

        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val actualRadius = if (roundPercent > 0) {
            // 使用百分比计算圆角
            val size = minOf(width, height)
            size * roundPercent / 200f
        } else {
            radius
        }

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, actualRadius, actualRadius, paint)

        return bitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
        messageDigest.update("radius:$radius,roundPercent:$roundPercent".toByteArray(Charsets.UTF_8))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RoundedCornersTransformation) return false
        return radius == other.radius && roundPercent == other.roundPercent
    }

    override fun hashCode(): Int {
        return ID.hashCode() + (radius * 1000).toInt() + (roundPercent * 1000).toInt()
    }
}