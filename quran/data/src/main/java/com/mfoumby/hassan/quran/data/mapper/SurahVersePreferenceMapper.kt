package com.mfoumby.hassan.quran.data.mapper

import com.google.gson.Gson
import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.model.LocalReciter
import com.mfoumby.hassan.quran.data.model.LocalSurahVerse
import com.mfoumby.hassan.quran.data.model.LocalSurahVersePreferences
import com.mfoumby.hassan.quran.domain.entity.ArabicTextFont
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences

private val gson = Gson()

fun LocalSurahVersePreferences.toSurahVersePreferences() = SurahVersePreferences(
    displayMode = SurahVersePreferences.DisplayMode.valueOf(displayMode),
    displayTajweed = displayTajweed,
    displayTranslation = displayTranslation,
    arabicTextFont = ArabicTextFont.valueOf(arabicTextFont),
    arabicTextFontSize = arabicTextFontSize,
    translationLanguage = translationLanguage?.let(Language::valueOf),
    reciter = gson.fromJson(reciter, LocalReciter::class.java)?.toReciter(),
    surahBookmark = gson.fromJson(surahBookmark, LocalSurahVerse::class.java)?.toSurahVerse(),
    juzBookmark = gson.fromJson(juzBookmark, LocalSurahVerse::class.java)?.toSurahVerse(),
    hizbBookmark = gson.fromJson(hizbBookmark, LocalSurahVerse::class.java)?.toSurahVerse()
)

fun SurahVersePreferences.toLocalSurahVersePreferences() = LocalSurahVersePreferences(
    displayMode = displayMode.name,
    displayTajweed = displayTajweed,
    displayTranslation = displayTranslation,
    arabicTextFont = arabicTextFont.name,
    arabicTextFontSize = arabicTextFontSize,
    translationLanguage = translationLanguage?.name,
    reciter = reciter?.let { gson.toJson(it.toLocal()) },
    surahBookmark = surahBookmark?.let { gson.toJson(it.toLocal()) },
    juzBookmark = juzBookmark?.let { gson.toJson(it.toLocal()) },
    hizbBookmark = hizbBookmark?.let { gson.toJson(it.toLocal()) }
)