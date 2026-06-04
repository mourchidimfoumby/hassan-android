package com.mfoumby.hassan

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mfoumby.hassan.quran.data.local.LocalSurah
import com.mfoumby.hassan.quran.data.local.SurahDao

@Database(
    entities = [LocalSurah::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun surahDao(): SurahDao
}