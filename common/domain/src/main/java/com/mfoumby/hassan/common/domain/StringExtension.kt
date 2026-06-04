package com.mfoumby.hassan.common.domain

fun String.capitalizeWords(): String =
    replace(Regex("(?<!\\p{L})\\p{L}")) {
        it.value.uppercase()
    }