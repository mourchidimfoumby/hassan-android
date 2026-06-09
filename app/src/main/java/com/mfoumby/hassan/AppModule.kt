package com.mfoumby.hassan

import androidx.room.Room
import com.mfoumby.hassan.ui.NavigationHostViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
                androidContext(),
                LocalDatabase::class.java,
                "LocalDatabase"
            ).fallbackToDestructiveMigration(true).build()
    }
    single { get<LocalDatabase>().surahDao() }
    single { get<LocalDatabase>().surahVerseDao() }
    single { get<LocalDatabase>().surahVerseTranslationDao() }

    viewModelOf(::NavigationHostViewModel)
}