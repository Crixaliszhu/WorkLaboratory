package com.yupao.happynewd.model

import androidx.annotation.Keep

@Keep
data class ResumePosterEntity(
    val imgPath: String?,
    val resumeBaseEntity: ResumeDetailBaseParamsEntity?,
) : PosterEntity()

sealed class PosterEntity