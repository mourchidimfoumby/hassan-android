package com.mfoumby.hassan.quran.data.mapper

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.model.LocalSurahVerseTranslation
import com.mfoumby.hassan.quran.data.model.RemoteSurahVerseTranslation
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation

fun LocalSurahVerseTranslation.toSurahVerseTranslation() = SurahVerseTranslation(
    number = number,
    surahNumber = surahNumber,
    translation = translation,
    language = Language.valueOf(language)
)

fun RemoteSurahVerseTranslation.toSurahVerseTranslation(language: Language) = SurahVerseTranslation(
    number = number,
    surahNumber = surahNumber,
    translation = translation,
    language = language
)

fun SurahVerseTranslation.toLocal() = LocalSurahVerseTranslation(
    number = number,
    surahNumber = surahNumber,
    translation = translation,
    language = language.name
)