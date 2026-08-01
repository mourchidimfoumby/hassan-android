package com.mfoumby.hassan.quran.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_PAGE
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_TABLE_NAME
import com.mfoumby.hassan.quran.data.model.LocalSurahVerse
import com.mfoumby.hassan.quran.data.model.LocalVerse

@Dao
interface VerseDao {
    @Transaction
    @Query("""
        SELECT * FROM $VERSE_TABLE_NAME 
        WHERE $VERSE_SURAH_NUMBER = :surahNumber 
        ORDER BY $VERSE_NUMBER
    """)
    suspend fun getSurahVerseFromNumber(surahNumber: Int): List<LocalSurahVerse>

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