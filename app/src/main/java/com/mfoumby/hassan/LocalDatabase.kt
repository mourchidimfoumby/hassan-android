package com.mfoumby.hassan

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mfoumby.hassan.quran.data.local.SurahDao
import com.mfoumby.hassan.quran.data.local.SurahVerseDao
import com.mfoumby.hassan.quran.data.local.SurahVerseTranslationDao
import com.mfoumby.hassan.quran.data.model.LocalSurah
import com.mfoumby.hassan.quran.data.model.LocalSurahVerse
import com.mfoumby.hassan.quran.data.model.LocalSurahVerseTranslation

@Database(
    entities = [
        LocalSurah::class,
        LocalSurahVerse::class,
        LocalSurahVerseTranslation::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun surahDao(): SurahDao
    abstract fun surahVerseDao(): SurahVerseDao
    abstract fun surahVerseTranslationDao(): SurahVerseTranslationDao
}