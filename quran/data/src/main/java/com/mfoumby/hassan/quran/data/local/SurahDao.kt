package com.mfoumby.hassan.quran.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mfoumby.hassan.quran.data.SurahField.Local.SURAH_NUMBER
import com.mfoumby.hassan.quran.data.SurahField.Local.SURAH_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface SurahDao {
    @Query("SELECT * FROM $SURAH_TABLE_NAME ORDER BY $SURAH_NUMBER")
    fun getSurahsFlow(): Flow<List<LocalSurah>>

    @Query("SELECT * FROM $SURAH_TABLE_NAME ORDER BY $SURAH_NUMBER")
    suspend fun getSurahs(): List<LocalSurah>

    @Upsert
    suspend fun upsertSurahs(surahs: List<LocalSurah>)
}