package com.mfoumby.hassan.quran.data.mapper

import com.google.gson.Gson
import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.model.LocalReciter
import com.mfoumby.hassan.quran.data.model.LocalSurahVersePreferences
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences

private val gson = Gson()

fun LocalSurahVersePreferences.toSurahVersePreferences() = SurahVersePreferences(
    displayTranslation = displayTranslation,
    translationLanguage = translationLanguage?.let(Language::valueOf),
    reciter = gson.fromJson(reciter, LocalReciter::class.java)?.toReciter()
)

fun SurahVersePreferences.toLocalSurahVersePreferences() = LocalSurahVersePreferences(
    displayTranslation = displayTranslation,
    translationLanguage = translationLanguage?.name,
    reciter = reciter?.let { gson.toJson(it.toLocal()) }
)