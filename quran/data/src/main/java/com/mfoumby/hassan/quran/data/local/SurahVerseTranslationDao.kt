package com.mfoumby.hassan.quran.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.LANGUAGE
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.TABLE_NAME
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.VERSE_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField
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
        SELECT VT.*
        FROM $TABLE_NAME VT
        INNER JOIN ${VerseField.Local.VERSE_TABLE_NAME} V 
            ON V.${VerseField.Local.VERSE_SURAH_NUMBER} = VT.$SURAH_NUMBER
            AND V.${VerseField.Local.VERSE_NUMBER} = VT.$VERSE_NUMBER
        WHERE V.${VerseField.Local.VERSE_JUZ} = :juzNumber 
            AND VT.$LANGUAGE = :language
        ORDER BY VT.$SURAH_NUMBER, VT.$VERSE_NUMBER
    """)
    suspend fun getSurahVerseTranslationsFromJuz(juzNumber: Int, language: String): List<LocalSurahVerseTranslation>

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