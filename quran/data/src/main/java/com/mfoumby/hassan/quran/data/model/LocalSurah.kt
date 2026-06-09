package com.mfoumby.hassan.quran.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mfoumby.hassan.quran.data.field.SurahField.Local.SURAH_NAME
import com.mfoumby.hassan.quran.data.field.SurahField.Local.SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.SurahField.Local.SURAH_TABLE_NAME
import com.mfoumby.hassan.quran.data.field.SurahField.Local.SURAH_TOTAL_VERSES
import com.mfoumby.hassan.quran.data.field.SurahField.Local.SURAH_TRANSLATION
import com.mfoumby.hassan.quran.data.field.SurahField.Local.SURAH_TRANSLITERATION
import com.mfoumby.hassan.quran.data.field.SurahField.Local.SURAH_TYPE

@Entity(tableName = SURAH_TABLE_NAME)
data class LocalSurah(
    @PrimaryKey
    @ColumnInfo(name = SURAH_NUMBER)
    val number: Int,
    @ColumnInfo(name = SURAH_NAME)
    val name: String,
    @ColumnInfo(name = SURAH_TRANSLITERATION)
    val transliteration: String,
    @ColumnInfo(name = SURAH_TOTAL_VERSES)
    val totalVerses: Int,
    @ColumnInfo(name = SURAH_TYPE)
    val type: String,
    @ColumnInfo(name = SURAH_TRANSLATION)
    val translation: String
)
