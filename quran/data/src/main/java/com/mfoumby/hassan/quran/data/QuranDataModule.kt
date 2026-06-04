package com.mfoumby.hassan.quran.data

import com.mfoumby.hassan.quran.data.local.SurahLocalDataSource
import com.mfoumby.hassan.quran.data.remote.SurahApi
import com.mfoumby.hassan.quran.data.remote.SurahApiImpl
import com.mfoumby.hassan.quran.data.remote.SurahRepositoryImpl
import com.mfoumby.hassan.quran.data.remote.SurahRemoteDataSource
import com.mfoumby.hassan.quran.data.worker.StartupQuranWorker
import com.mfoumby.hassan.quran.domain.SurahRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val quranDataModule = module {
    singleOf(::SurahApiImpl) { bind<SurahApi>() }
    singleOf(::SurahRemoteDataSource)
    singleOf(::SurahLocalDataSource)
    singleOf(::SurahRepositoryImpl) { bind<SurahRepository>() }
    factory { StartupQuranWorker(context = androidContext()) }
}