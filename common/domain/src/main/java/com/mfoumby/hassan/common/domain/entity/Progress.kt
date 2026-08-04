package com.mfoumby.hassan.common.domain.entity

data class Progress(
    private val current: Int,
    private val total: Int
) {
    val progress: Float
        get() = current.toFloat() / total.toFloat()
}