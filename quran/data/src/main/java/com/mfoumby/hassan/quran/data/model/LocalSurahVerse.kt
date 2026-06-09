package com.mfoumby.hassan.quran.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mfoumby.hassan.quran.data.field.SurahVerseField.Local.SURAH_VERSE_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseField.Local.SURAH_VERSE_SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseField.Local.SURAH_VERSE_TABLE_NAME
import com.mfoumby.hassan.quran.data.field.SurahVerseField.Local.SURAH_VERSE_TEXT

@Entity(
    tableName = SURAH_VERSE_TABLE_NAME,
    primaryKeys = [SURAH_VERSE_NUMBER, SURAH_VERSE_SURAH_NUMBER]
)
data class LocalSurahVerse(
    @ColumnInfo(name = SURAH_VERSE_NUMBER)
    val number: Int,
    @ColumnInfo(name = SURAH_VERSE_SURAH_NUMBER)
    val surahNumber: Int,
    @ColumnInfo(name = SURAH_VERSE_TEXT)
    val text: String
)
