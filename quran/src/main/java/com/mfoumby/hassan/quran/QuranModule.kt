package com.mfoumby.hassan.quran

import com.mfoumby.hassan.quran.ui.QuranViewModel
import com.mfoumby.hassan.quran.ui.surahverse.SurahVerseViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val quranModule = module {
    viewModelOf(::QuranViewModel)
    viewModelOf(::SurahVerseViewModel)
}