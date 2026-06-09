package com.mfoumby.hassan.quran.data.model

import com.google.firebase.firestore.PropertyName
import com.mfoumby.hassan.quran.data.field.SurahField

data class RemoteSurah(
    @get:PropertyName(SurahField.Remote.SURAH_NUMBER)
    @set:PropertyName(SurahField.Remote.SURAH_NUMBER)
    var number: Int = 0,
    @get:PropertyName(SurahField.Remote.SURAH_NAME)
    @set:PropertyName(SurahField.Remote.SURAH_NAME)
    var name: String = "",
    @get:PropertyName(SurahField.Remote.SURAH_TRANSLITERATION)
    @set:PropertyName(SurahField.Remote.SURAH_TRANSLITERATION)
    var transliteration: String = "",
    @get:PropertyName(SurahField.Remote.SURAH_TYPE)
    @set:PropertyName(SurahField.Remote.SURAH_TYPE)
    var type: String = "",
    @get:PropertyName(SurahField.Remote.SURAH_TOTAL_VERSES)
    @set:PropertyName(SurahField.Remote.SURAH_TOTAL_VERSES)
    var totalVerses: Int = 0
)