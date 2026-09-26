package com.mfoumby.hassan.quran.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.HIZB_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.JUZ_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.LANGUAGE
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.TABLE_NAME
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.TRANSLATION
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.VERSE_NUMBER

@Entity(
    tableName = TABLE_NAME,
    primaryKeys = [VERSE_NUMBER, SURAH_NUMBER, LANGUAGE]
)
data class LocalSurahVerseTranslation(
    @ColumnInfo(name = VERSE_NUMBER)
    val verseNumber: Int,
    @ColumnInfo(name = SURAH_NUMBER)
    val surahNumber: Int,
    @ColumnInfo(name = JUZ_NUMBER)
    val juzNumber: Int,
    @ColumnInfo(name = HIZB_NUMBER)
    val hizbNumber: Int,
    @ColumnInfo(name = TRANSLATION)
    val translation: String,
    @ColumnInfo(name = LANGUAGE)
    val language: String,
)
