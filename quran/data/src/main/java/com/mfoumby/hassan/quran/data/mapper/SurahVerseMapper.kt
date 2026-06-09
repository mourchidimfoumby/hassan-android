package com.mfoumby.hassan.quran.data.mapper

import com.mfoumby.hassan.quran.data.model.LocalSurahVerse
import com.mfoumby.hassan.quran.data.model.RemoteSurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVerse

fun RemoteSurahVerse.toSurahVerse() = SurahVerse(
    number = number,
    surahNumber = surahNumber,
    text = text
)

fun LocalSurahVerse.toSurahVerse() = SurahVerse(
    number = number,
    surahNumber = surahNumber,
    text = text
)

fun SurahVerse.toLocal() = LocalSurahVerse(
    number = number,
    surahNumber = surahNumber,
    text = text
)