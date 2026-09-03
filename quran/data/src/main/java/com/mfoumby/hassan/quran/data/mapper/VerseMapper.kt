package com.mfoumby.hassan.quran.data.mapper

import com.mfoumby.hassan.quran.data.model.LocalVerse
import com.mfoumby.hassan.quran.data.model.RemoteVerse
import com.mfoumby.hassan.quran.domain.entity.Verse

fun RemoteVerse.toVerse() = Verse(
    verseNumber = number,
    surahNumber = surahNumber,
    text = text,
    transliteration = transliteration,
    page = page,
    juzNumber = juz,
    hizbNumber = hizb
)

fun LocalVerse.toVerse() = Verse(
    verseNumber = verseNumber,
    surahNumber = surahNumber,
    text = text,
    transliteration = transliteration,
    page = page,
    juzNumber = juz,
    hizbNumber = hizb
)

fun Verse.toLocal() = LocalVerse(
    verseNumber = verseNumber,
    surahNumber = surahNumber,
    text = text,
    transliteration = transliteration,
    page = page,
    juz = juzNumber,
    hizb = hizbNumber
)