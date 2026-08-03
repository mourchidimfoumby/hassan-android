package com.mfoumby.hassan.quran.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.mfoumby.hassan.quran.data.field.HizbField
import com.mfoumby.hassan.quran.data.field.JuzField
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_HIZB
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_JUZ
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_PAGE
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_TABLE_NAME
import com.mfoumby.hassan.quran.data.model.LocalHizb
import com.mfoumby.hassan.quran.data.model.LocalJuz
import com.mfoumby.hassan.quran.data.model.LocalSurahVerse
import com.mfoumby.hassan.quran.data.model.LocalVerse
import kotlinx.coroutines.flow.Flow

@Dao
interface VerseDao {
    @Query("SELECT * FROM ${JuzField.Local.JUZ_VIEW_NAME}")
    fun getAllJuz(): Flow<List<LocalJuz>>

    @Query("SELECT * FROM ${HizbField.Local.HIZB_VIEW_NAME}")
    fun getAllHizb(): Flow<List<LocalHizb>>

    @Transaction
    @Query("""
        SELECT * FROM $VERSE_TABLE_NAME 
        WHERE $VERSE_SURAH_NUMBER = :surahNumber 
        ORDER BY $VERSE_SURAH_NUMBER, $VERSE_NUMBER
        LIMIT :limit
    """)
    suspend fun getSurahVerseFromSurah(surahNumber: Int, limit: Int): List<LocalSurahVerse>

    @Transaction
    @Query("""
        SELECT * FROM $VERSE_TABLE_NAME
        WHERE $VERSE_JUZ = :juzNumber
        ORDER BY $VERSE_SURAH_NUMBER, $VERSE_NUMBER
        LIMIT :limit
    """)
    suspend fun getSurahVersesFromJuz(juzNumber: Int, limit: Int): List<LocalSurahVerse>

    @Transaction
    @Query("""
        SELECT * FROM $VERSE_TABLE_NAME
        WHERE $VERSE_HIZB = :hizbNumber
        ORDER BY $VERSE_SURAH_NUMBER, $VERSE_NUMBER
        LIMIT :limit
    """)
    suspend fun getSurahVersesFromHizb(hizbNumber: Int, limit: Int): List<LocalSurahVerse>

    @Transaction
    @Query("""
        SELECT * FROM $VERSE_TABLE_NAME 
        WHERE $VERSE_PAGE = :page 
        ORDER BY $VERSE_SURAH_NUMBER, $VERSE_NUMBER
    """)
    suspend fun getSurahVersesFromPage(page: Int): List<LocalSurahVerse>

    @Transaction
    @Query("""
        SELECT * FROM $VERSE_TABLE_NAME 
        WHERE $VERSE_SURAH_NUMBER = :surahNumber 
        AND $VERSE_NUMBER = :verseNumber
    """)
    suspend fun getSurahVerse(surahNumber: Int, verseNumber: Int): LocalSurahVerse?

    @Query("SELECT COUNT(*) FROM $VERSE_TABLE_NAME")
    suspend fun getVersesCount(): Int

    @Upsert
    suspend fun upsertVerses(surahVerses: List<LocalVerse>)
}