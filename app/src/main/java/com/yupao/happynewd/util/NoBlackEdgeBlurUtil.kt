package com.yupao.happynewd.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

/**
 * 无黑边模糊工具类
 * 解决图片模糊变换后出现黑边的问题
 */
object NoBlackEdgeBlurUtil {

    /**
     * 无黑边模糊处理
     * @param bitmap 原始图片
     * @param radius 模糊半径 (1-25)
     * @return 处理后的图片
     */
    fun blurWithoutBlackEdge(bitmap: Bitmap, radius: Int): Bitmap {
        val clampedRadius = radius.coerceIn(1, 25)
        
        // 添加边距，边距大小为模糊半径
        val padding = clampedRadius
        val paddedWidth = bitmap.width + padding * 2
        val paddedHeight = bitmap.height + padding * 2
        
        // 创建带边距的bitmap
        val paddedBitmap = Bitmap.createBitmap(paddedWidth, paddedHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(paddedBitmap)
        
        val paint = Paint().apply {
            isFilterBitmap = true
            isAntiAlias = true
        }
        
        // 绘制原图到中心
        canvas.drawBitmap(bitmap, padding.toFloat(), padding.toFloat(), paint)
        
        // 填充边缘像素
        fillEdgePixels(canvas, bitmap, padding, paint)
        
        // 执行模糊
        val blurredBitmap = fastBlur(paddedBitmap, clampedRadius)
        
        // 裁剪回原尺寸
        val result = Bitmap.createBitmap(blurredBitmap, padding, padding, bitmap.width, bitmap.height)
        
        // 清理临时bitmap
        paddedBitmap.recycle()
        blurredBitmap.recycle()
        
        return result
    }
    
    /**
     * 填充边缘像素，避免黑边
     */
    private fun fillEdgePixels(canvas: Canvas, bitmap: Bitmap, padding: Int, paint: Paint) {
        val width = bitmap.width
        val height = bitmap.height
        
        // 左边缘
        val leftEdge = Bitmap.createBitmap(bitmap, 0, 0, 1, height)
        for (i in 0 until padding) {
            canvas.drawBitmap(leftEdge, i.toFloat(), padding.toFloat(), paint)
        }
        
        // 右边缘
        val rightEdge = Bitmap.createBitmap(bitmap, width - 1, 0, 1, height)
        for (i in 0 until padding) {
            canvas.drawBitmap(rightEdge, (width + padding + i).toFloat(), padding.toFloat(), paint)
        }
        
        // 上边缘
        val topEdge = Bitmap.createBitmap(bitmap, 0, 0, width, 1)
        for (i in 0 until padding) {
            canvas.drawBitmap(topEdge, padding.toFloat(), i.toFloat(), paint)
        }
        
        // 下边缘
        val bottomEdge = Bitmap.createBitmap(bitmap, 0, height - 1, width, 1)
        for (i in 0 until padding) {
            canvas.drawBitmap(bottomEdge, padding.toFloat(), (height + padding + i).toFloat(), paint)
        }
        
        // 填充四个角落
        val topLeftPixel = bitmap.getPixel(0, 0)
        val topRightPixel = bitmap.getPixel(width - 1, 0)
        val bottomLeftPixel = bitmap.getPixel(0, height - 1)
        val bottomRightPixel = bitmap.getPixel(width - 1, height - 1)
        
        paint.color = topLeftPixel
        canvas.drawRect(0f, 0f, padding.toFloat(), padding.toFloat(), paint)
        
        paint.color = topRightPixel
        canvas.drawRect((width + padding).toFloat(), 0f, (width + padding * 2).toFloat(), padding.toFloat(), paint)
        
        paint.color = bottomLeftPixel
        canvas.drawRect(0f, (height + padding).toFloat(), padding.toFloat(), (height + padding * 2).toFloat(), paint)
        
        paint.color = bottomRightPixel
        canvas.drawRect((width + padding).toFloat(), (height + padding).toFloat(), 
                       (width + padding * 2).toFloat(), (height + padding * 2).toFloat(), paint)
    }
    
    /**
     * 快速模糊算法
     */
    private fun fastBlur(bitmap: Bitmap, radius: Int): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val pix = IntArray(w * h)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)
        
        val wm = w - 1
        val hm = h - 1
        val wh = w * h
        val div = radius + radius + 1
        
        val r = IntArray(wh)
        val g = IntArray(wh)
        val b = IntArray(wh)
        
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
}