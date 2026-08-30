package com.mfoumby.hassan.quran.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.mfoumby.hassan.common.R
import com.mfoumby.hassan.common.ui.theme.bodyUthmanic
import com.mfoumby.hassan.quran.domain.entity.ArabicTextFont

val ArabicTextFont.resId: Int
    get() = when (this) {
        ArabicTextFont.UTHMANIC -> R.string.uthmani
    }


val ArabicTextFont.typography: TextStyle
    @Composable
    get() = when (this) {
        ArabicTextFont.UTHMANIC -> MaterialTheme.typography.bodyUthmanic
    }