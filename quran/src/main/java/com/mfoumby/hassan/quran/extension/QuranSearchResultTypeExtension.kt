package com.mfoumby.hassan.quran.extension

import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.QuranSearchResultType

val QuranSearchResultType.labelResId: Int
    get() = when (this) {
        QuranSearchResultType.SURAH_RESULT -> R.string.surah
    }