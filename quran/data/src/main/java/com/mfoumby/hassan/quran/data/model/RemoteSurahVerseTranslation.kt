package com.mfoumby.hassan.quran.data.model

import com.google.firebase.firestore.PropertyName
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationField
import com.mfoumby.hassan.quran.data.field.SurahVerseTranslationsField

data class RemoteSurahVerseTranslation(
    @get:PropertyName(SurahVerseTranslationField.Remote.VERSE_NUMBER)
    @set:PropertyName(SurahVerseTranslationField.Remote.VERSE_NUMBER)
    var verseNumber: Int = 0,
    @get:PropertyName(SurahVerseTranslationField.Remote.SURAH_NUMBER)
    @set:PropertyName(SurahVerseTranslationField.Remote.SURAH_NUMBER)
    var surahNumber: Int = 0,
    @get:PropertyName(SurahVerseTranslationField.Remote.JUZ_NUMBER)
    @set:PropertyName(SurahVerseTranslationField.Remote.JUZ_NUMBER)
    var juzNumber: Int = 0,
    @get:PropertyName(SurahVerseTranslationField.Remote.HIZB_NUMBER)
    @set:PropertyName(SurahVerseTranslationField.Remote.HIZB_NUMBER)
    var hizbNumber: Int = 0,
    @get:PropertyName(SurahVerseTranslationField.Remote.TRANSLATION)
    @set:PropertyName(SurahVerseTranslationField.Remote.TRANSLATION)
    var translation: String = "",
    @get:PropertyName(SurahVerseTranslationField.Remote.LANGUAGE)
    @set:PropertyName(SurahVerseTranslationField.Remote.LANGUAGE)
    var language: String = ""
)

data class RemoteSurahVerseTranslations(
    @get:PropertyName(SurahVerseTranslationsField.Remote.VALUES)
    @set:PropertyName(SurahVerseTranslationsField.Remote.VALUES)
    var values: List<RemoteSurahVerseTranslation> = emptyList()
)