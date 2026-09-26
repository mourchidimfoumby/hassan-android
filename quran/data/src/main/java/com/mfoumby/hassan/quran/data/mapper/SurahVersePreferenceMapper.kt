package com.mfoumby.hassan.quran.data.mapper

import com.google.gson.Gson
import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.model.LocalReciter
import com.mfoumby.hassan.quran.data.model.LocalSurahVerse
import com.mfoumby.hassan.quran.data.model.LocalSurahVersePreferences
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences

private val gson = Gson()

fun LocalSurahVersePreferences.toSurahVersePreferences() = SurahVersePreferences(
    displayMode = SurahVersePreferences.DisplayMode.valueOf(displayMode),
    translationLanguage = translationLanguage?.let(Language::valueOf),
    displayTransliteration = displayTransliteration,
    displayTranslation = displayTranslation,
    displayTajweed = displayTajweed,
    reciter = gson.fromJson(reciter, LocalReciter::class.java)?.toReciter(),
    audioAutomaticScrolling = audioAutomaticScrolling,
    surahVerseBookmark = gson.fromJson(surahVerseBookmark, LocalSurahVerse::class.java)?.toSurahVerse()
)

fun SurahVersePreferences.toLocalSurahVersePreferences() = LocalSurahVersePreferences(
    displayMode = displayMode.name,
    translationLanguage = translationLanguage?.name,
    displayTransliteration = displayTransliteration,
    displayTranslation = displayTranslation,
    displayTajweed = displayTajweed,
    reciter = reciter?.let { gson.toJson(it.toLocal()) },
    audioAutomaticScrolling = audioAutomaticScrolling,
    surahVerseBookmark = surahVerseBookmark?.let { gson.toJson(it.toLocal()) }
)