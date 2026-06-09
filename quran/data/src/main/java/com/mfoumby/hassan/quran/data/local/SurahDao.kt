package com.mfoumby.hassan.quran.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mfoumby.hassan.quran.data.field.SurahField.Local.SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahField.Local.SURAH_TABLE_NAME
import com.mfoumby.hassan.quran.data.model.LocalSurah
import kotlinx.coroutines.flow.Flow

@Dao
interface SurahDao {
    @Query("SELECT * FROM $SURAH_TABLE_NAME ORDER BY $SURAH_NUMBER")
    fun getSurahs(): Flow<List<LocalSurah>>

    @Query("SELECT * FROM $SURAH_TABLE_NAME WHERE $SURAH_NUMBER = :surahNumber")
    fun getSurah(surahNumber: Int): LocalSurah

    @Query("SELECT COUNT(*) FROM $SURAH_TABLE_NAME")
    suspend fun getSurahCount(): Int

    @Upsert
    suspend fun upsertSurahs(surahs: List<LocalSurah>)
}