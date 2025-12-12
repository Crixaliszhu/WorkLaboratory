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
            //用需要创建高斯模糊bitmap创建一个空的bitmap
            val outBitmap =
                Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888)
            // 初始化Renderscript，该类提供了RenderScript context，创建其他RS类之前必须先创建这个类，其控制RenderScript的初始化，资源管理及释放
            val rs = RenderScript.create(context)
            // 创建高斯模糊对象
            val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            // 创建Allocations，此类是将数据传递给RenderScript内核的主要方 法，并制定一个后备类型存储给定类型
            val allIn = Allocation.createFromBitmap(rs, bitmap)
            val allOut = Allocation.createFromBitmap(rs, outBitmap)
            //设定模糊度(注：Radius最大只能设置25.f)
            blurScript.setRadius(25f)
            // Perform the Renderscript
            blurScript.setInput(allIn)
            blurScript.forEach(allOut)
            // Copy the final bitmap created by the out Allocation to the outBitmap
            allOut.copyTo(outBitmap)
            // recycle the original bitmap
            // bitmap.recycle();
            // After finishing everything, we destroy the Renderscript.
            rs.destroy()
            Handler(Looper.getMainLooper()).post(object : Runnable {
                override fun run() {
                    result.onComplete(outBitmap)
                }
            })
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