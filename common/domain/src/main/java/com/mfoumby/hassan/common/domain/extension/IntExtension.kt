package com.mfoumby.hassan.common.domain.extension

import kotlin.math.roundToInt

fun Int.half() = (this / 2f).roundToInt()

fun Int.asIndex() = this - 1

fun Int.fromIndex() = this + 1