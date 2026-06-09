package com.mfoumby.hassan.quran.data.mapper

import com.mfoumby.hassan.quran.data.model.LocalSurah
import com.mfoumby.hassan.quran.data.model.RemoteSurah
import com.mfoumby.hassan.quran.domain.entity.Surah

fun RemoteSurah.toSurah(translation: String) = Surah(
    number = number,
    name = name,
    transliteration = transliteration,
    type = type,
    totalVerses = totalVerses,
    translation = translation
)

fun LocalSurah.toSurah() = Surah(
    number = number,
    name = name,
    transliteration = transliteration,
    type = type,
    totalVerses = totalVerses,
    translation = translation
)

fun Surah.toLocal() = LocalSurah(
    number = number,
    name = name,
    transliteration = transliteration,
    type = type,
    totalVerses = totalVerses,
    translation = translation
)