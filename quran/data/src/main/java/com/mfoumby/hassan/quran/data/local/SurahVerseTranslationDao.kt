package com.mfoumby.hassan.quran.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.HIZB_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.JUZ_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.LANGUAGE
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.TABLE_NAME
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.VERSE_NUMBER
import com.mfoumby.hassan.quran.data.model.LocalSurahVerseTranslation

@Dao
interface SurahVerseTranslationDao {
    @Query("""
        SELECT *
        FROM $TABLE_NAME
        WHERE $SURAH_NUMBER = :surahNumber 
        AND $LANGUAGE = :language
        ORDER BY $VERSE_NUMBER
    """)
    suspend fun getSurahVerseTranslations(surahNumber: Int, language: String): List<LocalSurahVerseTranslation>

    @Query("""
        SELECT *
        FROM $TABLE_NAME
        WHERE $JUZ_NUMBER = :juzNumber 
        AND $LANGUAGE = :language
        ORDER BY $SURAH_NUMBER, $VERSE_NUMBER
    """)
    suspend fun getSurahVerseTranslationsFromJuz(juzNumber: Int, language: String): List<LocalSurahVerseTranslation>

    @Query("""
        SELECT *
        FROM $TABLE_NAME
        WHERE $HIZB_NUMBER = :hizbNumber 
        AND $LANGUAGE = :language
        ORDER BY $SURAH_NUMBER, $VERSE_NUMBER
    """)
    suspend fun getSurahVerseTranslationsFromHizb(hizbNumber: Int, language: String): List<LocalSurahVerseTranslation>

    @Query("""
        SELECT COUNT(*)
        FROM $TABLE_NAME
        WHERE $LANGUAGE = :language
    """)
    suspend fun getSurahVerseTranslationCount(language: String): Int


    @Upsert
    suspend fun upsertSurahVerseTranslations(surahVerseTranslations: List<LocalSurahVerseTranslation>)

    @Query("DELETE FROM $TABLE_NAME WHERE $LANGUAGE = :language")
    suspend fun deleteSurahVerseTranslation(language: String)
}