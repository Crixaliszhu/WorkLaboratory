package com.yupao.happynewd.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.yupao.happynewd.R
import com.yupao.happynewd.databinding.ShareLayoutMiniCoverBinding
import com.yupao.happynewd.model.ResumePosterEntity
import com.yupao.happynewd.ui.PictureFileUtils
import com.yupao.happynewd.util.DensityUtils
import com.yupao.happynewd.view.ResizedImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CoverGenerateHelper {

    @Suppress("YuPaoFunParameterRule")
    private fun generateResumeView(
        context: Context?,
        owner: LifecycleOwner,
        inflater: LayoutInflater,
        posterEntity: ResumePosterEntity
    ): View? {
        val binding =
            DataBindingManager.inflate(ShareLayoutMiniCoverBinding.inflate(inflater), owner) {
                it.entity = posterEntity.resumeBaseEntity
                it.executePendingBindings()
            }.binding
        val view = binding?.root
        val w = DensityUtils.dp2px(context, 210f)
        val h = DensityUtils.dp2px(context, 170f)
        view?.measure(
            View.MeasureSpec.makeMeasureSpec(
                w,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                h,
                View.MeasureSpec.EXACTLY
            )
        )
        view?.layout(0, 0, view.measuredWidth, view.measuredHeight)
        return view
    }

    private fun generateCoverBitmap(view: View?): Bitmap? {
        view ?: return null

        // 创建白色背景的bitmap
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 填充白色背景（防止透明区域显示为黑色）
        canvas.drawColor(android.graphics.Color.WHITE)

        // 绘制view
        view.draw(canvas)
        return bitmap
    }

    suspend fun generateResumeCover(
        context: Context?,
        owner: LifecycleOwner,
        inflater: LayoutInflater,
        posterEntity: ResumePosterEntity,
        callBack: (String) -> Unit,
    ) {
        if (context == null) {
            callBack.invoke("")
            return
        }
        
        withContext(Dispatchers.Main) {
            val view = generateResumeView(context, owner, inflater, posterEntity)
            val imageView = view?.findViewById<ResizedImageView>(R.id.ivImageView)
            
            if (imageView != null && !posterEntity.resumeBaseEntity?.avatar.isNullOrBlank()) {
                // 等待图片加载完成
                Glide.with(context)
                    .asBitmap()
                    .load(posterEntity.resumeBaseEntity?.avatar)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                            // 图片加载完成后生成封面
                            val bmp = generateCoverBitmap(view)
                            if (bmp != null) {
                                PictureFileUtils.saveBitmapLocalPng(context, bmp)
                                val url = PictureFileUtils.saveBitmapToProviderCache(context, bmp)
                                callBack.invoke(url)
                            } else {
                                callBack.invoke("")
                            }
                        }
                        
                        override fun onLoadCleared(placeholder: Drawable?) {
                            callBack.invoke("")
                        }
                    })
            } else {
                // 没有头像，直接生成
                val bmp = generateCoverBitmap(view)
                if (bmp != null) {
                    PictureFileUtils.saveBitmapLocalPng(context, bmp)
                    val url = PictureFileUtils.saveBitmapToProviderCache(context, bmp)
                    callBack.invoke(url)
                } else {
                    callBack.invoke("")
                }
            }
        }
    }

}