package com.mfoumby.hassan.quran.data.mapper

import com.mfoumby.hassan.quran.data.model.LocalSurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVerse

fun SurahVerse.toLocal() = LocalSurahVerse(
    verse = verse.toLocal(),
    surah = surah.toLocal()
)

fun LocalSurahVerse.toSurahVerse() = SurahVerse(
    surah = surah.toSurah(),
    verse = verse.toVerse()
)