package com.mfoumby.hassan.quran.data

import com.mfoumby.hassan.quran.data.local.LocalSurah
import com.mfoumby.hassan.quran.data.model.RemoteSurah
import com.mfoumby.hassan.quran.domain.Surah

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