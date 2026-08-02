package com.mfoumby.hassan.quran.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.mfoumby.hassan.quran.data.field.JuzField
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_JUZ
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_PAGE
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_TABLE_NAME
import com.mfoumby.hassan.quran.data.model.LocalJuz
import com.mfoumby.hassan.quran.data.model.LocalSurahVerse
import com.mfoumby.hassan.quran.data.model.LocalVerse
import kotlinx.coroutines.flow.Flow

@Dao
interface VerseDao {
    @Query("SELECT * FROM ${JuzField.Local.JUZ_VIEW_NAME}")
    fun getAllJuz(): Flow<List<LocalJuz>>

    @Transaction
    @Query("""
        SELECT * FROM $VERSE_TABLE_NAME 
        WHERE $VERSE_SURAH_NUMBER = :surahNumber 
        ORDER BY $VERSE_NUMBER
        LIMIT :limit
    """)
    suspend fun getSurahVerseFromNumber(surahNumber: Int, limit: Int): List<LocalSurahVerse>

    @Transaction
    @Query("""
        SELECT * FROM $VERSE_TABLE_NAME
        WHERE $VERSE_JUZ = :juzNumber
        ORDER BY $VERSE_SURAH_NUMBER, $VERSE_NUMBER
        LIMIT :limit
    """)
    suspend fun getSurahVerseFromJuzNumber(juzNumber: Int, limit: Int): List<LocalSurahVerse>

    @Transaction
    @Query("""
        SELECT * FROM $VERSE_TABLE_NAME 
        WHERE $VERSE_PAGE = :page 
        ORDER BY $VERSE_NUMBER
    """)
    suspend fun getSurahVerseFromPage(page: Int): List<LocalSurahVerse>

    @Query("SELECT COUNT(*) FROM $VERSE_TABLE_NAME")
    suspend fun getVersesCount(): Int

    @Upsert
    suspend fun upsertVerses(surahVerses: List<LocalVerse>)
}