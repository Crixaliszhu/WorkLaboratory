package com.yupao.happynewd.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.yupao.happynewd.util.OSSImageUtil.blur
import java.security.MessageDigest

class BlurTransformation @JvmOverloads constructor(
    private val radius: Int = MAX_RADIUS,
    private val sampling: Int = DEFAULT_DOWN_SAMPLING
) : BitmapTransformation() {
    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val scaledWidth = width / sampling
        val scaledHeight = height / sampling
        
        // 添加边距避免黑边
        val padding = radius
        val paddedWidth = scaledWidth + padding * 2
        val paddedHeight = scaledHeight + padding * 2
        
        var bitmap = pool[paddedWidth, paddedHeight, Bitmap.Config.ARGB_8888]
        val canvas = Canvas(bitmap)
        
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG
        
        // 绘制到中心位置
        canvas.scale(1 / sampling.toFloat(), 1 / sampling.toFloat())
        canvas.drawBitmap(toTransform, padding.toFloat() * sampling, padding.toFloat() * sampling, paint)
        
        // 模糊处理
        bitmap = blur(bitmap, radius, true)!!
        
        // 裁剪回原尺寸
        val finalBitmap = pool[scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888]
        val finalCanvas = Canvas(finalBitmap)
        finalCanvas.drawBitmap(bitmap, -padding.toFloat(), -padding.toFloat(), paint)
        
        return finalBitmap
    }

    override fun toString(): String {
        return "BlurTransformation(radius=$radius, sampling=$sampling)"
    }

    override fun equals(o: Any?): Boolean {
        return o is BlurTransformation && o.radius == radius && o.sampling == sampling
    }

    override fun hashCode(): Int {
        return ID.hashCode() + radius * 1000 + sampling * 10
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + radius + sampling).toByteArray(CHARSET))
    }

    companion object {
        private const val VERSION = 1
        private const val ID = "BlurTransformation." + VERSION
        private const val MAX_RADIUS = 25
        private const val DEFAULT_DOWN_SAMPLING = 1
    }
}