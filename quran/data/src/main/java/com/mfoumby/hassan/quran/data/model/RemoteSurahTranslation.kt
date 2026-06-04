package com.mfoumby.hassan.quran.data.model

import com.google.firebase.firestore.PropertyName
import com.mfoumby.hassan.quran.data.SurahTranslationField
import com.mfoumby.hassan.quran.data.SurahTranslationsField

data class RemoteSurahTranslations(
    @get:PropertyName(SurahTranslationsField.VALUES)
    @set:PropertyName(SurahTranslationsField.VALUES)
    var values: List<RemoteSurahTranslation> = emptyList()
)

data class RemoteSurahTranslation(
    @get:PropertyName(SurahTranslationField.SURAH_NUMBER)
    @set:PropertyName(SurahTranslationField.SURAH_NUMBER)
    var number: Int = 0,
    @get:PropertyName(SurahTranslationField.SURAH_TRANSLATION)
    @set:PropertyName(SurahTranslationField.SURAH_TRANSLATION)
    var translation: String = ""
)
