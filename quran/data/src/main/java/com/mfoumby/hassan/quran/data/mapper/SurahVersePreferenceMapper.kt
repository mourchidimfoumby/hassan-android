package com.mfoumby.hassan.quran.data.mapper

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.model.LocalSurahVersePreferences
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences

fun LocalSurahVersePreferences.toSurahVersePreferences(): SurahVersePreferences {
    return SurahVersePreferences(
        displayTranslation = displayTranslation,
        translationLanguage = translationLanguage?.let(Language::valueOf)
    )
}

fun SurahVersePreferences.toLocalSurahVersePreferences() = LocalSurahVersePreferences(
    displayTranslation = displayTranslation,
    translationLanguage = translationLanguage?.name
)