package com.mfoumby.hassan.common

import com.mfoumby.hassan.common.domain.entity.Language

val Language.resId: Int
    get() = when (this) {
        Language.ENGLISH -> R.string.english
        Language.FRENCH -> R.string.french
        Language.ARABIC -> R.string.arabic
    }

val Language.roundedFlagResId: Int
    get() = when (this) {
        Language.ENGLISH -> R.drawable.england_rounded_flag
        Language.FRENCH -> R.drawable.france_rounded_flag
        Language.ARABIC -> R.drawable.saudi_arabia_rounded_flag
    }