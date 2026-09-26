package com.mfoumby.hassan.quran.data.mapper

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.model.LocalSurahVerseTranslation
import com.mfoumby.hassan.quran.data.model.RemoteSurahVerseTranslation
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation

fun LocalSurahVerseTranslation.toSurahVerseTranslation() = SurahVerseTranslation(
    verseNumber = verseNumber,
    surahNumber = surahNumber,
    juzNumber = juzNumber,
    hizbNumber = hizbNumber,
    translation = translation,
    language = Language.valueOf(language)
)

fun RemoteSurahVerseTranslation.toSurahVerseTranslation(language: Language) = SurahVerseTranslation(
    verseNumber = verseNumber,
    surahNumber = surahNumber,
    juzNumber = juzNumber,
    hizbNumber = hizbNumber,
    translation = translation,
    language = language
)

fun SurahVerseTranslation.toLocal() = LocalSurahVerseTranslation(
    verseNumber = verseNumber,
    surahNumber = surahNumber,
    juzNumber = juzNumber,
    hizbNumber = hizbNumber,
    translation = translation,
    language = language.name
)