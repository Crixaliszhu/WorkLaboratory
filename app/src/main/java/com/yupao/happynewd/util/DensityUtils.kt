package com.yupao.happynewd.util

import android.annotation.TargetApi
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Build

object DensityUtils {
    private const val WIDTH = 375f //参考设备的宽，单位是dp
    private var appDensity: Float = 0f //表示屏幕密度
    private var appScaleDensity: Float = 0f //字体缩放比例，默认appDensity

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun createConfigurationContext(context: Context?): Context? {
        if (context == null) return null
        val config = context.resources.configuration
        val targetDensity = context.resources.displayMetrics.widthPixels / WIDTH
        val targetDensityDpi = (targetDensity * 160).toInt()
        config.densityDpi = targetDensityDpi
        config.setTo(config)
        return context.createConfigurationContext(config)
    }


    fun setDensity(application: Application) {
        //获取当前app的屏幕显示信息
        val displayMetrics = application.resources.displayMetrics
        if (appDensity == 0f) {
            //初始化赋值操作
            appDensity = displayMetrics.density
            appScaleDensity = displayMetrics.scaledDensity

            //添加字体变化监听回调
            application.registerComponentCallbacks(object : ComponentCallbacks {

                override fun onLowMemory() {

                }

                override fun onConfigurationChanged(newConfig: Configuration) {
                    //字体发生更改，重新对scaleDensity进行赋值
                    if (newConfig.fontScale > 0) {
                        appScaleDensity = application.resources.displayMetrics.scaledDensity
                    }
                }
            })
        }
    }

    fun getFontChangeScale(): Float {
        return if (appScaleDensity == 0f) {
            0f
        } else {
            appScaleDensity / appDensity
        }
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @return
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale: Float = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    fun dp2px(context: Context?, dipValue: Float): Int {
        if (context == null) {
            return 0
        }
        val scale: Float = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale: Float = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 将dp值转换为sp值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    fun dp2sp(context: Context, dpValue: Float): Int {
        return px2sp(context, dp2px(context, dpValue).toFloat())
    }
}
