package com.yupao.happynewd.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.load.MultiTransformation
import com.yupao.happynewd.util.BlurTransformation
import com.yupao.happynewd.util.RoundedCornersTransformation
import com.yupao.happynewd.util.OSSImageUtil
import com.yupao.happynewd.util.ResourceGlideUrl

/**
 * 从rn模块拷贝过来的，不然会循环依赖
 *
 * 创建时间：2025/2/19
 *
 * @author fc
 */
class ResizedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : androidx.constraintlayout.utils.widget.ImageFilterView(context, attrs, defStyleAttr) {

    private var currentCacheKey: String = "" // 当前加载的资源信息

    private var imageUrl: String? = null // 图片原始 URL

    private var errorImg: Any? = null
    private var placeHolder: Any? = null

    private var blurRadius: Int? = null
    private var cornerRadius: Float = 0f
    private var roundPercent: Float = 0f

    /**
     * 设置图片 URL 并加载图片
     * @param url 图片原始 URL
     */
    fun setResizedImageUrl(url: String?) {
        imageUrl = url
        loadResizedImage()
    }

    fun setErrorImg(img: Any?) {
        this.errorImg = img
        invalidate()
    }

    fun setPlaceHolder(img: Drawable?) {
        this.placeHolder = img
        invalidate()
    }

    fun setBlurRadius(blurRadius: Int?) {
        this.blurRadius = blurRadius
        invalidate()
    }

    override fun setRound(round: Float) {
        super.setRound(round)
        this.cornerRadius = round
        loadResizedImage()
    }

    override fun setRoundPercent(roundPercent: Float) {
        super.setRoundPercent(roundPercent)
        this.roundPercent = roundPercent
        loadResizedImage()
    }

    /**
     * 根据当前控件宽度加载缩略图
     */
    @SuppressLint("CheckResult")
    private fun loadResizedImage() {
        val width = measuredWidth
        if (width <= 0 || imageUrl.isNullOrBlank()) {
            return
        }
        val resizedUrl = OSSImageUtil.generateResizedImageUrl(imageUrl, width)
        val resourceId = OSSImageUtil.getResourceId(imageUrl)
        val realUrl = ResourceGlideUrl(
            url = resizedUrl,
            resourceId = resourceId,
            width = width
        )
        // 防止重复加载相同资源
        if (realUrl.cacheKey == this.currentCacheKey) {
            println("当前加载的图片和当前加载的图片一致，不加载图片")
            return
        }

        // 异常生命周期的异常捕获
        try {
            if (!isValidContextForGlide(context)) {
                println("在异常状态下加载图片，不加载")
                return
            }
            Glide.with(context)
                .load(realUrl)
                .apply {
                    val transformations = mutableListOf<com.bumptech.glide.load.Transformation<android.graphics.Bitmap>>()
                    
                    // 添加圆角变换
                    if (cornerRadius > 0 || roundPercent > 0) {
                        // 如果是圆形（roundPercent = 100），先裁剪为正方形
                        if (roundPercent >= 100f) {
                            transformations.add(com.bumptech.glide.load.resource.bitmap.CenterCrop())
                            transformations.add(com.bumptech.glide.load.resource.bitmap.CircleCrop())
                        } else {
                            transformations.add(RoundedCornersTransformation(cornerRadius, roundPercent))
                        }
                    }
                    
                    // 添加模糊变换
                    if (blurRadius != null) {
                        transformations.add(BlurTransformation(radius = blurRadius!!))
                    }
                    
                    if (transformations.isNotEmpty()) {
                        if (transformations.size == 1) {
                            apply(RequestOptions.bitmapTransform(transformations[0]))
                        } else {
                            apply(RequestOptions.bitmapTransform(MultiTransformation(*transformations.toTypedArray())))
                        }
                    }
                }
                .error(this.errorImg)
                .placeholder(this.placeHolder as? Drawable)
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
                        this@ResizedImageView.currentCacheKey = realUrl.cacheKey
                        when (dataSource) {
                            DataSource.MEMORY_CACHE -> {
                                // 从内存缓存加载
                                println("加载图片来源：内存缓存")
                            }

                            DataSource.REMOTE -> {
                                // 从网络加载
                                println("加载图片来源：网络")
                            }

                            DataSource.DATA_DISK_CACHE -> {
                                // 从数据磁盘缓存加载（如原始文件）
                                println("加载图片来源：数据磁盘缓存")
                            }

                            DataSource.LOCAL -> {
                                // 从本地资源加载（如文件、URI）
                                println("加载图片来源：本地资源")
                            }

                            DataSource.RESOURCE_DISK_CACHE -> {
                                // 从本地资源加载（如文件、URI）
                                println("加载图片来源：本地资源缓存")
                            }

                            else -> {
                                println("未知来源")
                            }
                        }
                        return false // 返回 false 表示继续传递回调
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(this)
        } catch (e: Throwable) {
            // 捕获加载异常
            println(
                "加载图片异常${e}"
            )
        }
    }

    private fun isValidContextForGlide(context: Context): Boolean {
        val activity: Activity? = getActivityFromContext(context)
        return if (activity == null) {
            true
        } else {
            !isActivityDestroyed(activity)
        }
    }

    private fun getActivityFromContext(context: Context): Activity? {
        if (context is Activity) {
            return context
        } else {
            if (context is ContextWrapper) {
                val wrapperBaseContext = context.baseContext
                if (wrapperBaseContext is Activity) {
                    return wrapperBaseContext
                }
            }

            return null
        }
    }

    private fun isActivityDestroyed(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= 17) {
            activity.isDestroyed() || activity.isFinishing()
        } else {
            activity.isFinishing() || activity.isChangingConfigurations()
        }
    }

    /**
     * 监听控件大小变化
     * 在宽度变化时重新加载图片
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw) {
            loadResizedImage()
        }
    }
}