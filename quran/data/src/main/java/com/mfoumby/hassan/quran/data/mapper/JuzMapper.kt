package com.mfoumby.hassan.quran.data.mapper

import com.mfoumby.hassan.quran.data.model.LocalJuz
import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.SurahVerse

fun LocalJuz.toJuz() = Juz(
    number = number,
    firstSurahVerse = SurahVerse(
        surah = firstSurah.toSurah(),
        verse = firstVerse.toVerse()
    )
)