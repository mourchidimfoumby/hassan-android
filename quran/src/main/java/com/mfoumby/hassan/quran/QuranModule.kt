package com.mfoumby.hassan.quran

import com.mfoumby.hassan.quran.ui.QuranViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val quranModule = module {
    viewModelOf(::QuranViewModel)
}