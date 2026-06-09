package com.mfoumby.hassan.quran.data

import com.mfoumby.hassan.quran.data.local.SurahLocalDataSource
import com.mfoumby.hassan.quran.data.local.SurahVerseLocalDataSource
import com.mfoumby.hassan.quran.data.local.SurahVerseTranslationLocalDataSource
import com.mfoumby.hassan.quran.data.remote.SurahApi
import com.mfoumby.hassan.quran.data.remote.SurahApiImpl
import com.mfoumby.hassan.quran.data.remote.SurahRemoteDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseApi
import com.mfoumby.hassan.quran.data.remote.SurahVerseApiImpl
import com.mfoumby.hassan.quran.data.remote.SurahVerseRemoteDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationApi
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationApiImpl
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationRemoteDataSource
import com.mfoumby.hassan.quran.data.repository.SurahRepositoryImpl
import com.mfoumby.hassan.quran.data.repository.SurahVerseRepositoryImpl
import com.mfoumby.hassan.quran.data.repository.SurahVerseTranslationRepositoryImpl
import com.mfoumby.hassan.quran.data.worker.StartupQuranWorker
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val quranDataModule = module {
    singleOf(::SurahApiImpl) { bind<SurahApi>() }
    singleOf(::SurahRemoteDataSource)
    singleOf(::SurahLocalDataSource)
    singleOf(::SurahRepositoryImpl) { bind<SurahRepository>() }

    singleOf(::SurahVerseApiImpl) { bind<SurahVerseApi>() }
    singleOf(::SurahVerseRemoteDataSource)
    singleOf(::SurahVerseLocalDataSource)
    singleOf(::SurahVerseRepositoryImpl) { bind<SurahVerseRepository>() }

    singleOf(::SurahVerseTranslationApiImpl) { bind<SurahVerseTranslationApi>() }
    singleOf(::SurahVerseTranslationRemoteDataSource)
    singleOf(::SurahVerseTranslationLocalDataSource)
    singleOf(::SurahVerseTranslationRepositoryImpl) { bind<SurahVerseTranslationRepository>() }

    factory { StartupQuranWorker(context = androidContext()) }
}