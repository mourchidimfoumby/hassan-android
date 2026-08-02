package com.mfoumby.hassan

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mfoumby.hassan.quran.data.local.SurahDao
import com.mfoumby.hassan.quran.data.local.SurahVerseTranslationDao
import com.mfoumby.hassan.quran.data.local.VerseDao
import com.mfoumby.hassan.quran.data.model.LocalJuz
import com.mfoumby.hassan.quran.data.model.LocalSurah
import com.mfoumby.hassan.quran.data.model.LocalSurahVerseTranslation
import com.mfoumby.hassan.quran.data.model.LocalVerse

@Database(
    entities = [
        LocalSurah::class,
        LocalVerse::class,
        LocalSurahVerseTranslation::class
    ],
    views = [LocalJuz::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun surahDao(): SurahDao
    abstract fun surahVerseDao(): VerseDao
    abstract fun surahVerseTranslationDao(): SurahVerseTranslationDao
}