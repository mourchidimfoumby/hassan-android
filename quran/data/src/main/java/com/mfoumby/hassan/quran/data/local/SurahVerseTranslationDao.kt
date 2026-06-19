package com.mfoumby.hassan.quran.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.LANGUAGE
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.TABLE_NAME
import com.mfoumby.hassan.quran.data.model.LocalSurahVerseTranslation

@Dao
interface SurahVerseTranslationDao {
    @Query(
        "SELECT * " +
                "FROM $TABLE_NAME " +
                "WHERE $SURAH_NUMBER = :surahNumber " +
                "AND $LANGUAGE = :language"
    )
    suspend fun getSurahVerseTranslations(surahNumber: Int, language: String): List<LocalSurahVerseTranslation>

    @Query(
        "SELECT COUNT(*) " +
                "FROM $TABLE_NAME " +
                "WHERE $LANGUAGE = :language"
    )
    suspend fun getSurahVerseTranslationCount(language: String): Int


    @Upsert
    suspend fun upsertSurahVerseTranslations(surahVerseTranslations: List<LocalSurahVerseTranslation>)

    @Query("DELETE FROM $TABLE_NAME WHERE $LANGUAGE = :language")
    suspend fun deleteSurahVerseTranslation(language: String)
}