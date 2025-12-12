package com.yupao.happynewd.ui

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object PictureFileUtils {
    fun saveBitmapToProviderCache(context: Context, mBitmap: Bitmap): String {
        val filePic: File?
        try {
            filePic =
                File(getProviderSuperPath(context) + generateFileName() + ".jpg")
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs()
                filePic.createNewFile()
            }
            val fos = FileOutputStream(filePic)
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            return ""
        }
        return filePic.getAbsolutePath()
    }


    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    fun saveBitmapLocalPng(context: Context, mBitmap: Bitmap): String? {
        val filePic: File?
        val savePath = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)!!
            .getAbsolutePath() + "/yupao/pic"
        try {
            filePic = File(savePath + generateFileName() + ".jpg")
            if (!filePic.exists()) {
                filePic.getParentFile()?.mkdirs()
                filePic.createNewFile()
            }
            val fos = FileOutputStream(filePic)
            mBitmap.compress(CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            //插入相册
            PictureFileUtils.insertExternalPng(context, filePic, mBitmap)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return filePic.getAbsolutePath()
    }

    /**
     * 随机生产文件名
     *
     * @return
     */
    private fun generateFileName(): String {
        return System.currentTimeMillis().toString() + ""
    }


    /**
     * 获取分享Provider存储父路径
     */
    private fun getProviderSuperPath(context: Context): String {
        return context.getExternalFilesDir(null).toString() + "/yupaoShareFile/"
    }

    @Throws(IOException::class)
    private fun insertExternalPng(context: Context, file: File, bitmap: Bitmap) {
        //插入相册
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName())
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        val contentResolver = context.getContentResolver()
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            val out = contentResolver.openOutputStream(uri)
            bitmap.setHasAlpha(true)
            bitmap.compress(CompressFormat.PNG, 100, out)
            if (out != null) {
                out.flush()
                out.close()
            }
        }
    }
}