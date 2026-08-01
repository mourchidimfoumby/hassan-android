package com.mfoumby.hassan.quran.domain

import com.mfoumby.hassan.quran.domain.usecase.DeleteTranslationLanguageUseCase
import com.mfoumby.hassan.quran.domain.usecase.DownloadSurahVerseTranslationUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val quranDomainModule = module {
    factoryOf(::DeleteTranslationLanguageUseCase)
    factoryOf(::DownloadSurahVerseTranslationUseCase)
}