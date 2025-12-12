package com.yupao.happynewd.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

object BlurBitmapUtils {
    fun LoadUrlToBlueBg(context: Context, url: String?, result: CompleteListener) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    blurBitmap(context, resource, result)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }


    private fun blurBitmap(context: Context?, bitmap: Bitmap, result: CompleteListener) {
        Thread(Runnable {
            // 添加边距避免黑边
            val padding = 25
            val paddedWidth = bitmap.width + padding * 2
            val paddedHeight = bitmap.height + padding * 2
            
            // 创建带边距的bitmap
            val paddedBitmap = Bitmap.createBitmap(paddedWidth, paddedHeight, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(paddedBitmap)
            
            // 填充边缘像素
            val paint = android.graphics.Paint()
            paint.isFilterBitmap = true
            paint.isAntiAlias = true
            
            // 绘制原图到中心
            canvas.drawBitmap(bitmap, padding.toFloat(), padding.toFloat(), paint)
            
            // 填充边缘
            val matrix = android.graphics.Matrix()
            // 左边缘
            val leftEdge = Bitmap.createBitmap(bitmap, 0, 0, 1, bitmap.height)
            matrix.setScale(padding.toFloat(), 1f)
            canvas.drawBitmap(leftEdge, matrix, paint)
            
            // 右边缘
            val rightEdge = Bitmap.createBitmap(bitmap, bitmap.width - 1, 0, 1, bitmap.height)
            matrix.setTranslate((paddedWidth - padding).toFloat(), padding.toFloat())
            matrix.preScale(padding.toFloat(), 1f)
            canvas.drawBitmap(rightEdge, matrix, paint)
            
            // 上边缘
            val topEdge = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, 1)
            matrix.setTranslate(padding.toFloat(), 0f)
            matrix.preScale(1f, padding.toFloat())
            canvas.drawBitmap(topEdge, matrix, paint)
            
            // 下边缘
            val bottomEdge = Bitmap.createBitmap(bitmap, 0, bitmap.height - 1, bitmap.width, 1)
            matrix.setTranslate(padding.toFloat(), (paddedHeight - padding).toFloat())
            matrix.preScale(1f, padding.toFloat())
            canvas.drawBitmap(bottomEdge, matrix, paint)
            
            // 创建输出bitmap
            val outBitmap = Bitmap.createBitmap(paddedWidth, paddedHeight, Bitmap.Config.ARGB_8888)
            
            val rs = RenderScript.create(context)
            val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            val allIn = Allocation.createFromBitmap(rs, paddedBitmap)
            val allOut = Allocation.createFromBitmap(rs, outBitmap)
            
            blurScript.setRadius(25f)
            blurScript.setInput(allIn)
            blurScript.forEach(allOut)
            allOut.copyTo(outBitmap)
            
            // 裁剪回原尺寸
            val finalBitmap = Bitmap.createBitmap(outBitmap, padding, padding, bitmap.width, bitmap.height)
            
            rs.destroy()
            paddedBitmap.recycle()
            outBitmap.recycle()
            
            Handler(Looper.getMainLooper()).post {
                result.onComplete(finalBitmap)
            }
        }).start()
    }


    /**
     * 按照指定的5:4比例,对源Bitmap进行裁剪
     *
     * @param srcBitmap 源图片对应的Bitmap
     * @return Bitmap
     */
    fun cropBitmap5To4(srcBitmap: Bitmap): Bitmap? {
        var desWidth = srcBitmap.getWidth()
        var desHeight = srcBitmap.getHeight()
        val desRate = desWidth.toFloat() / desHeight
        val rate = 5f / 4f
        if (desRate > rate) { //宽有多余
            desWidth = (desHeight * 5 / 4)
        } else { //宽有不够，裁剪高度
            desHeight = (desWidth * 4 / 5)
        }
        return topCenterCrop(srcBitmap, desWidth, desHeight)
    }

    /**
     * 按照指定的宽高比例,对源Bitmap进行裁剪 适用于简历分享
     *
     * @param srcBitmap 源图片对应的Bitmap
     * @return Bitmap
     */
    fun cropBitmapFormShareResume(srcBitmap: Bitmap): Bitmap? {
        val desWidth = srcBitmap.getWidth()
        var desHeight = srcBitmap.getHeight()
        val desRate = desWidth.toFloat() / desHeight
        val rate = 311f / 357
        // 宽高比 小于0.87 需要裁剪高度
        if (desRate < rate) {
            desHeight = (desWidth * 357 / 311)
        }
        return topCenterCrop(srcBitmap, desWidth, desHeight)
    }

    /**
     * 按照指定的宽高比例,对源Bitmap进行裁剪
     * 注意，输出的Bitmap只是宽高比与指定宽高比相同，大小未必相同
     *
     * @param srcBitmap 源图片对应的Bitmap
     * @param desWidth  目标图片宽度
     * @param desHeight 目标图片高度
     * @return Bitmap
     */
    private fun topCenterCrop(srcBitmap: Bitmap, desWidth: Int, desHeight: Int): Bitmap? {
        val srcWidth = srcBitmap.getWidth()
        val srcHeight = srcBitmap.getHeight()
        var newWidth = srcWidth
        var newHeight = srcHeight
        val srcRate = srcWidth.toFloat() / srcHeight
        val desRate = desWidth.toFloat() / desHeight
        var dx = 0
        val dy = 0
        if (srcRate == desRate) {
            return srcBitmap
        } else if (srcRate > desRate) {
            newWidth = (srcHeight * desRate).toInt()
            dx = (srcWidth - newWidth) / 2
        } else {
            newHeight = (srcWidth / desRate).toInt()
            //            dy = (srcHeight - newHeight) / 2;
        }
        //创建目标Bitmap，并用选取的区域来绘制
        val desBitmap = Bitmap.createBitmap(srcBitmap, dx, dy, newWidth, newHeight)
        return desBitmap
    }

    interface CompleteListener {
        fun onComplete(result: Bitmap?)
    }
}