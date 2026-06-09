package com.mfoumby.hassan.quran.data.model

import com.google.firebase.firestore.PropertyName
import com.mfoumby.hassan.quran.data.field.SurahTranslationField
import com.mfoumby.hassan.quran.data.field.SurahTranslationsField

data class RemoteSurahTranslation(
    @get:PropertyName(SurahTranslationField.Remote.SURAH_NUMBER)
    @set:PropertyName(SurahTranslationField.Remote.SURAH_NUMBER)
    var number: Int = 0,
    @get:PropertyName(SurahTranslationField.Remote.SURAH_TRANSLATION)
    @set:PropertyName(SurahTranslationField.Remote.SURAH_TRANSLATION)
    var translation: String = ""
)

data class RemoteSurahTranslations(
    @get:PropertyName(SurahTranslationsField.Remote.VALUES)
    @set:PropertyName(SurahTranslationsField.Remote.VALUES)
    var values: List<RemoteSurahTranslation> = emptyList()
)