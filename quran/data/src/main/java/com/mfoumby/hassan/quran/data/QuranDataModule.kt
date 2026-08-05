package com.mfoumby.hassan.quran.data

import com.mfoumby.hassan.quran.data.local.ReciterDataStore
import com.mfoumby.hassan.quran.data.local.ReciterLocalDataSource
import com.mfoumby.hassan.quran.data.local.SurahLocalDataSource
import com.mfoumby.hassan.quran.data.local.SurahVerseAudioFileStorage
import com.mfoumby.hassan.quran.data.local.SurahVerseAudioLocalDataSource
import com.mfoumby.hassan.quran.data.local.SurahVerseLocalDataSource
import com.mfoumby.hassan.quran.data.local.SurahVersePreferencesDataStore
import com.mfoumby.hassan.quran.data.local.SurahVersePreferencesLocalDataSource
import com.mfoumby.hassan.quran.data.local.SurahVerseTranslationLanguageDataStore
import com.mfoumby.hassan.quran.data.local.SurahVerseTranslationLanguageLocalDataSource
import com.mfoumby.hassan.quran.data.local.SurahVerseTranslationLocalDataSource
import com.mfoumby.hassan.quran.data.remote.ReciterApi
import com.mfoumby.hassan.quran.data.remote.ReciterApiImpl
import com.mfoumby.hassan.quran.data.remote.ReciterRemoteDataSource
import com.mfoumby.hassan.quran.data.remote.SurahApi
import com.mfoumby.hassan.quran.data.remote.SurahApiImpl
import com.mfoumby.hassan.quran.data.remote.SurahRemoteDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseAudioApi
import com.mfoumby.hassan.quran.data.remote.SurahVerseAudioApiImpl
import com.mfoumby.hassan.quran.data.remote.SurahVerseAudioRemoteDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseRemoteDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationApi
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationApiImpl
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationLanguageApi
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationLanguageApiImpl
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationLanguageRemoteDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationRemoteDataSource
import com.mfoumby.hassan.quran.data.remote.VerseApi
import com.mfoumby.hassan.quran.data.remote.VerseApiImpl
import com.mfoumby.hassan.quran.data.repository.ReciterRepositoryImpl
import com.mfoumby.hassan.quran.data.repository.SurahRepositoryImpl
import com.mfoumby.hassan.quran.data.repository.SurahVerseAudioRepositoryImpl
import com.mfoumby.hassan.quran.data.repository.SurahVersePreferencesRepositoryImpl
import com.mfoumby.hassan.quran.data.repository.SurahVerseRepositoryImpl
import com.mfoumby.hassan.quran.data.repository.SurahVerseTranslationLanguageRepositoryImpl
import com.mfoumby.hassan.quran.data.repository.SurahVerseTranslationRepositoryImpl
import com.mfoumby.hassan.quran.data.worker.StartupQuranWorker
import com.mfoumby.hassan.quran.domain.repository.ReciterRepository
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseAudioRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationLanguageRepository
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

    singleOf(::SurahVersePreferencesDataStore)
    singleOf(::SurahVersePreferencesLocalDataSource)
    singleOf(::SurahVersePreferencesRepositoryImpl) { bind<SurahVersePreferencesRepository>() }

    singleOf(::VerseApiImpl) { bind<VerseApi>() }
    singleOf(::SurahVerseRemoteDataSource)
    singleOf(::SurahVerseLocalDataSource)
    singleOf(::SurahVerseRepositoryImpl) { bind<SurahVerseRepository>() }

    singleOf(::SurahVerseTranslationApiImpl) { bind<SurahVerseTranslationApi>() }
    singleOf(::SurahVerseTranslationRemoteDataSource)
    singleOf(::SurahVerseTranslationLocalDataSource)
    singleOf(::SurahVerseTranslationRepositoryImpl) { bind<SurahVerseTranslationRepository>() }

    singleOf(::SurahVerseTranslationLanguageApiImpl) { bind<SurahVerseTranslationLanguageApi>() }
    singleOf(::SurahVerseTranslationLanguageRemoteDataSource)
    singleOf(::SurahVerseTranslationLanguageLocalDataSource)
    singleOf(::SurahVerseTranslationLanguageDataStore)
    singleOf(::SurahVerseTranslationLanguageRepositoryImpl) { bind<SurahVerseTranslationLanguageRepository>() }

    singleOf(::SurahVerseAudioApiImpl) { bind<SurahVerseAudioApi>() }
    singleOf(::SurahVerseAudioRemoteDataSource)
    singleOf(::SurahVerseAudioLocalDataSource)
    single { SurahVerseAudioFileStorage(context = androidContext()) }
    singleOf(::SurahVerseAudioRepositoryImpl) { bind<SurahVerseAudioRepository>() }

    singleOf(::ReciterApiImpl) { bind<ReciterApi>() }
    singleOf(::ReciterRemoteDataSource)
    singleOf(::ReciterLocalDataSource)
    singleOf(::ReciterDataStore)
    singleOf(::ReciterRepositoryImpl) { bind<ReciterRepository>() }

    factory { StartupQuranWorker(context = androidContext()) }
}