package com.mfoumby.hassan.quran.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_HIZB
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_JUZ
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_PAGE
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_TABLE_NAME
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_TEXT

@Entity(
    tableName = VERSE_TABLE_NAME,
    indices = [
        Index(value = [VERSE_JUZ, VERSE_SURAH_NUMBER, VERSE_NUMBER], name = "idx_$VERSE_JUZ"),
        Index(value = [VERSE_HIZB, VERSE_SURAH_NUMBER, VERSE_NUMBER], name = "idx_$VERSE_HIZB")
    ],
    primaryKeys = [VERSE_NUMBER, VERSE_SURAH_NUMBER]
)
data class LocalVerse(
    @ColumnInfo(name = VERSE_NUMBER)
    val number: Int,
    @ColumnInfo(name = VERSE_SURAH_NUMBER)
    val surahNumber: Int,
    @ColumnInfo(name = VERSE_TEXT)
    val text: String,
    @ColumnInfo(name = VERSE_PAGE)
    val page: Int,
    @ColumnInfo(name = VERSE_JUZ)
    val juz: Int,
    @ColumnInfo(name = VERSE_HIZB)
    val hizb: Int
)
