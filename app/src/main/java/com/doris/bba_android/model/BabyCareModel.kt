package com.doris.bba_android.model

data class BabyCareModel(
    val stage: String? = null,
    val resume: String? = null,
    val activities: List<String>? = emptyList(),
    val careTips: List<String>? = emptyList(),
    val feedingTips: List<String>? = emptyList(),
    val sleepTips: List<String>? = emptyList()
)
