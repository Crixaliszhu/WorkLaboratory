package com.yupao.happynewd.util

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.yupao.happynewd.R
import com.yupao.happynewd.view.ResizedImageView

object IMAvatarUtils {
    /**
     * 设置头像
     */
    fun setAvatar(imageView: ResizedImageView?, avatarUrl: String?) {
        imageView ?: return
        if (avatarUrl.isNullOrBlank()) return
        imageView.setPlaceHolder(
            ContextCompat.getDrawable(
                imageView.context,
                R.drawable.ic_recruitment_head
            )
        )
        imageView.roundPercent = 100f
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setResizedImageUrl(avatarUrl)
        imageView.setBlurRadius(15)
    }
}