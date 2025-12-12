package com.yupao.happynewd.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

/**
 * @author guoqingshan
 * @date 2024/11/23/周六
 * @description
 */
object OSSImageUtil {

    /**
     * 将图片Url处理成带缩略图参数的URL
     * @param baseUrl String?
     * @param width Int
     * @return String
     */
    fun generateResizedImageUrl(baseUrl: String?, width: Int): String {
        if (baseUrl.isNullOrBlank()) return ""
        val resizeParam =
            "x-oss-process=image/resize,w_${getStandWidth(width)},limit_0"
        return if (baseUrl.contains("?")) {
            "$baseUrl&$resizeParam"
        } else {
            "$baseUrl?$resizeParam"
        }
    }

    /**
     * 从图片Url中提取 resourceId
     * @param url 图片 URL
     * @return resourceId 或 null
     */
    fun getResourceId(url: String?): String {
        // 如果URL为空或不包含最后的/，直接返回空
        if (url.isNullOrBlank() || !url.startsWith("http") || !url.contains("/r/")) {
            return ""
        }
        // 获取最后一个 / 的位置
        val lastSlashIndex = url.lastIndexOf("/")
        if (lastSlashIndex == url.length - 1) {
            return "" // 如果 / 是最后一个字符，返回空
        }
        // 判断是否包含 ? （即是否为私有资源）
        val queryIndex = url.indexOf("?") // 第一个 `?` 的位置，如果没有则为 -1
        // 计算结束位置
        val endIndex = if (queryIndex > 0) {
            queryIndex // 如果有 `?`，截取到 `?` 之前
        } else {
            url.length // 如果没有 `?`，截取到字符串末尾
        }
        // 截取文件名（不含路径）
        val fileName = url.substring(lastSlashIndex + 1, endIndex)
        // 去掉扩展名
        val resourceId = fileName.substringBefore(".")
        // 校验resourceId长度，确保在32-60位之间
        if (resourceId.length !in 32..60) {
            return ""
        }
        return resourceId
    }

    /**
     * 基于图片组件的大小获取缩略图宽度
     * @param width Int
     * @return Int
     */
    fun getStandWidth(width: Int): Int {
        return maxOf(((width + 99) / 100) * 100, 100)
    }

    fun blur(sentBitmap: Bitmap, radius: Int, canReuseInBitmap: Boolean): Bitmap? {
        val bitmap: Bitmap
        bitmap = if (canReuseInBitmap) {
            sentBitmap
        } else {
            sentBitmap.copy(sentBitmap.config, true)
        }
        if (radius < 1) {
            return null
        }
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
        var rsum: Int
        var gsum: Int
        var bsum: Int
        var x: Int
        var y: Int
        var i: Int
        var p: Int
        var yp: Int
        var yi: Int
        var yw: Int
        val vmin = IntArray(Math.max(w, h))
        var divsum = div + 1 shr 1
        divsum *= divsum
        val dv = IntArray(256 * divsum)
        i = 0
        while (i < 256 * divsum) {
            dv[i] = i / divsum
            i++
        }
        yi = 0
        yw = yi
        val stack = Array(div) { IntArray(3) }
        var stackpointer: Int
        var stackstart: Int
        var sir: IntArray
        var rbs: Int
        val r1 = radius + 1
        var routsum: Int
        var goutsum: Int
        var boutsum: Int
        var rinsum: Int
        var ginsum: Int
        var binsum: Int
        y = 0
        while (y < h) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            i = -radius
            while (i <= radius) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))]
                sir = stack[i + radius]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rbs = r1 - Math.abs(i)
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
                i++
            }
            stackpointer = radius
            x = 0
            while (x < w) {
                r[yi] = dv[rsum]
                g[yi] = dv[gsum]
                b[yi] = dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm)
                }
                p = pix[yw + vmin[x]]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer % div]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi++
                x++
            }
            yw += w
            y++
        }
        x = 0
        while (x < w) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            yp = -radius * w
            i = -radius
            while (i <= radius) {
                yi = Math.max(0, yp) + x
                sir = stack[i + radius]
                sir[0] = r[yi]
                sir[1] = g[yi]
                sir[2] = b[yi]
                rbs = r1 - Math.abs(i)
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
                i++
            }
            yi = x
            stackpointer = radius
            y = 0
            while (y < h) {

                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] =
                    -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w
                }
                p = x + vmin[y]
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
                sir = stack[stackpointer]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi += w
                y++
            }
            x++
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h)
        return bitmap
    }

    fun loadResizedImage(
        context: Context,
        imageView: ImageView,
        imageUrl: String,
        width: Int,
        errorImg: Drawable,
        placeHolder: Drawable
    ) {
        val resizedUrl = generateResizedImageUrl(imageUrl, width)
        val resourceId = getResourceId(imageUrl)
        val realUrl = ResourceGlideUrl(
            url = resizedUrl,
            resourceId = resourceId,
            width = width
        )
        println("异常生命周期的异常捕获 Glide $realUrl")
        // 异常生命周期的异常捕获
        try {
            Glide.with(context)
                .load(realUrl)
                .apply {
                    val transformations =
                        mutableListOf<com.bumptech.glide.load.Transformation<android.graphics.Bitmap>>()

                    // 添加圆角变换
                    transformations.add(
                        RoundedCornersTransformation(
                            radius = 1f,
                            roundPercent = 100f
                        )
                    )

                    // 添加模糊变换
                    transformations.add(BlurTransformation(radius = 20))

                    if (transformations.isNotEmpty()) {
                        if (transformations.size == 1) {
                            apply(RequestOptions.bitmapTransform(transformations[0]))
                        } else {
                            apply(RequestOptions.bitmapTransform(MultiTransformation(*transformations.toTypedArray())))
                        }
                    }
                }
                .error(errorImg)
                .placeholder(placeHolder)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>,
                        isFirstResource: Boolean,
                    ): Boolean {
                        println("ERROR:${e}")
                        // 图片加载失败
                        return false // 返回 false 表示继续传递回调
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean,
                    ): Boolean {
                        return false // 返回 false 表示继续传递回调
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView)
        } catch (e: Throwable) {
            // 捕获加载异常
            println(
                "加载图片异常${e}"
            )
        }
    }
}