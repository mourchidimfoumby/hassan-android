package com.mfoumby.hassan.common.domain.entity

data class Progress(
    val current: Int,
    val total: Int
) {
    val progress: Float
        get() = current.toFloat() / total.toFloat()
}