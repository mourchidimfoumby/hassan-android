package com.mfoumby.hassan.quran.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.LANGUAGE
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.TABLE_NAME
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField.Local.TRANSLATION

@Entity(
    tableName = TABLE_NAME,
    primaryKeys = [NUMBER, SURAH_NUMBER, LANGUAGE]
)
data class LocalSurahVerseTranslation(
    @ColumnInfo(name = NUMBER)
    val number: Int,
    @ColumnInfo(name = SURAH_NUMBER)
    val surahNumber: Int,
    @ColumnInfo(name = LANGUAGE)
    val language: String,
    @ColumnInfo(name = TRANSLATION)
    val translation: String
)
