package com.yupao.happynewd.util

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * 先模糊再圆形裁剪的变换，解决黑边问题
 */
class BlurThenCircleTransformation @JvmOverloads constructor(
    private val blurRadius: Int = 10
) : BitmapTransformation() {

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        // 1. 先居中裁剪为正方形
        val size = minOf(toTransform.width, toTransform.height)
        val x = (toTransform.width - size) / 2
        val y = (toTransform.height - size) / 2
        val squareBitmap = Bitmap.createBitmap(toTransform, x, y, size, size)
        
        // 2. 再模糊
        val blurredBitmap = fastBlur(squareBitmap, blurRadius)
        
        // 3. 最后圆形裁剪
        val result = getCircleBitmap(blurredBitmap)
        
        if (squareBitmap != toTransform) squareBitmap.recycle()
        if (blurredBitmap != squareBitmap) blurredBitmap.recycle()
        
        return result
    }
    
    private fun fastBlur(bitmap: Bitmap, radius: Int): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val pix = IntArray(w * h)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)
        
        val wm = w - 1
        val hm = h - 1
        val div = radius + radius + 1
        val r = IntArray(w * h)
        val g = IntArray(w * h)
        val b = IntArray(w * h)
        
        val vmin = IntArray(maxOf(w, h))
        var divsum = (div + 1) shr 1
        divsum *= divsum
        val dv = IntArray(256 * divsum)
        for (i in 0 until 256 * divsum) {
            dv[i] = i / divsum
        }
        
        var yi = 0
        var yw = 0
        val stack = Array(div) { IntArray(3) }
        val r1 = radius + 1
        
        // 水平模糊
        for (y in 0 until h) {
            var rinsum = 0
            var ginsum = 0
            var binsum = 0
            var routsum = 0
            var goutsum = 0
            var boutsum = 0
            var rsum = 0
            var gsum = 0
            var bsum = 0
            
            for (i in -radius..radius) {
                val p = pix[yi + minOf(wm, maxOf(i, 0))]
                val sir = stack[i + radius]
                sir[0] = (p and 0xff0000) shr 16
                sir[1] = (p and 0x00ff00) shr 8
                sir[2] = p and 0x0000ff
                val rbs = r1 - kotlin.math.abs(i)
                rsum += sir[0] * rbs
                gsum += sir[1] * rbs
                bsum += sir[2] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
            }
            
            var stackpointer = radius
            for (x in 0 until w) {
                r[yi] = dv[rsum]
                g[yi] = dv[gsum]
                b[yi] = dv[bsum]
                
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                
                val stackstart = stackpointer - radius + div
                val sir = stack[stackstart % div]
                
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                
                if (y == 0) {
                    vmin[x] = minOf(x + radius + 1, wm)
                }
                val p = pix[yw + vmin[x]]
                
                sir[0] = (p and 0xff0000) shr 16
                sir[1] = (p and 0x00ff00) shr 8
                sir[2] = p and 0x0000ff
                
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                
                stackpointer = (stackpointer + 1) % div
                val sir2 = stack[stackpointer % div]
                
                routsum += sir2[0]
                goutsum += sir2[1]
                boutsum += sir2[2]
                
                rinsum -= sir2[0]
                ginsum -= sir2[1]
                binsum -= sir2[2]
                
                yi++
            }
            yw += w
        }
        
        // 垂直模糊
        for (x in 0 until w) {
            var rinsum = 0
            var ginsum = 0
            var binsum = 0
            var routsum = 0
            var goutsum = 0
            var boutsum = 0
            var rsum = 0
            var gsum = 0
            var bsum = 0
            var yp = -radius * w
            
            for (i in -radius..radius) {
                yi = maxOf(0, yp) + x
                val sir = stack[i + radius]
                sir[0] = r[yi]
                sir[1] = g[yi]
                sir[2] = b[yi]
                val rbs = r1 - kotlin.math.abs(i)
                rsum += r[yi] * rbs
                gsum += g[yi] * rbs
                bsum += b[yi] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                if (i < hm) {
                    yp += w
                }
            }
            
            yi = x
            var stackpointer = radius
            for (y in 0 until h) {
                pix[yi] = (0xff000000.toInt() and pix[yi]) or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
                
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                
                val stackstart = stackpointer - radius + div
                val sir = stack[stackstart % div]
                
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                
                if (x == 0) {
                    vmin[y] = minOf(y + r1, hm) * w
                }
                val p = x + vmin[y]
                
                sir[0] = r[p]
                sir[1] = g[p]
                sir[2] = b[p]
                
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                
                stackpointer = (stackpointer + 1) % div
                val sir2 = stack[stackpointer]
                
                routsum += sir2[0]
                goutsum += sir2[1]
                boutsum += sir2[2]
                
                rinsum -= sir2[0]
                ginsum -= sir2[1]
                binsum -= sir2[2]
                
                yi += w
            }
        }
        
        val result = bitmap.copy(bitmap.config, true)
        result.setPixels(pix, 0, w, 0, 0, w, h)
        return result
    }
    
    private fun getCircleBitmap(bitmap: Bitmap): Bitmap {
        val size = minOf(bitmap.width, bitmap.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        
        val paint = Paint().apply {
            isAntiAlias = true
            shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
        
        val radius = size / 2f
        canvas.drawCircle(radius, radius, radius, paint)
        
        return output
    }

    override fun toString(): String = "BlurThenCircleTransformation(blurRadius=$blurRadius)"
    override fun equals(other: Any?): Boolean = other is BlurThenCircleTransformation && other.blurRadius == blurRadius
    override fun hashCode(): Int = ID.hashCode() + blurRadius * 1000
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + blurRadius).toByteArray(CHARSET))
    }

    companion object {
        private const val ID = "BlurThenCircleTransformation.1"
    }
}