package com.mfoumby.hassan.common.domain.extension

import kotlin.math.roundToInt

fun Int.half(): Int = (this / 2f).roundToInt()

fun Int.asIndex(): Int = this - 1

fun Int.fromIndex(): Int = this + 1