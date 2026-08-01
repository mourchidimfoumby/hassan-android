package com.mfoumby.hassan.quran.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.mfoumby.hassan.quran.data.field.SurahField.Local.SURAH_NUMBER
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_SURAH_NUMBER

data class LocalSurahVerse(
    @Embedded val verse: LocalVerse,
    @Relation(
        parentColumn = VERSE_SURAH_NUMBER,
        entityColumn = SURAH_NUMBER
    )
    val surah: LocalSurah
)
