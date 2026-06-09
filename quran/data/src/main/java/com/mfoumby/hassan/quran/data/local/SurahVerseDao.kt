package com.mfoumby.hassan.quran.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mfoumby.hassan.quran.data.field.SurahVerseField.Local.SURAH_VERSE_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseField.Local.SURAH_VERSE_SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseField.Local.SURAH_VERSE_TABLE_NAME
import com.mfoumby.hassan.quran.data.model.LocalSurahVerse

@Dao
interface SurahVerseDao {
    @Query("SELECT * FROM $SURAH_VERSE_TABLE_NAME WHERE $SURAH_VERSE_SURAH_NUMBER = :surahNumber ORDER BY $SURAH_VERSE_NUMBER")
    suspend fun getSurahVerses(surahNumber: Int): List<LocalSurahVerse>

    @Query("SELECT COUNT(*) FROM $SURAH_VERSE_TABLE_NAME")
    suspend fun getSurahVersesCount(): Int

    @Upsert
    suspend fun upsertSurahVerses(surahVerses: List<LocalSurahVerse>)
}