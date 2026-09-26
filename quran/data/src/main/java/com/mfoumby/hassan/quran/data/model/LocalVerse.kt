package com.mfoumby.hassan.quran.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_HIZB_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_JUZ_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_PAGE
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_TABLE_NAME
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_TEXT
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_TRANSLITERATION

@Entity(
    tableName = VERSE_TABLE_NAME,
    indices = [
        Index(value = [VERSE_JUZ_NUMBER, VERSE_SURAH_NUMBER, VERSE_NUMBER], name = "idx_$VERSE_JUZ_NUMBER"),
        Index(value = [VERSE_HIZB_NUMBER, VERSE_SURAH_NUMBER, VERSE_NUMBER], name = "idx_$VERSE_HIZB_NUMBER")
    ],
    primaryKeys = [VERSE_NUMBER, VERSE_SURAH_NUMBER]
)
data class LocalVerse(
    @ColumnInfo(name = VERSE_NUMBER)
    val verseNumber: Int,
    @ColumnInfo(name = VERSE_SURAH_NUMBER)
    val surahNumber: Int,
    @ColumnInfo(name = VERSE_TEXT)
    val text: String,
    @ColumnInfo(name = VERSE_TRANSLITERATION)
    val transliteration: String,
    @ColumnInfo(name = VERSE_PAGE)
    val page: Int,
    @ColumnInfo(name = VERSE_JUZ_NUMBER)
    val juzNumber: Int,
    @ColumnInfo(name = VERSE_HIZB_NUMBER)
    val hizbNumber: Int
)
