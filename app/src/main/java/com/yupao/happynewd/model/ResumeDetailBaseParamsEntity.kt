package com.yupao.happynewd.model

import androidx.annotation.Keep

/**
 * 简历详情分享基础信息：
 * 头像，
 * 性别，
 * 姓名，
 * 工龄，
 * 最高学历，
 * 是否模糊，
 * 意向工作城市-市，
 * 最近工作经历-职位，
 * 最近工作经历-公司，
 * 自我介绍，
 */
@Keep
data class ResumeDetailBaseParamsEntity(
    val avatar: String?,
    val gender: Int?,
    val name: String?,
    val workAge: String?,
    val degree: String?,
    val isFuzzy: Boolean?,
    val hopeCity: String?,
    val jobTitle: String?,
    val jobCompany: String?,
    val introduction: String?,
) {

    /** 是否有工作经历 */
    fun hasExperience() = !jobTitle.isNullOrEmpty() && !jobCompany.isNullOrEmpty()

    /** 模糊度 */
    fun blurRadius() = if (isFuzzy == true) 20 else 0

}
