package com.mfoumby.hassan.quran.data.model

import com.google.firebase.firestore.PropertyName
import com.mfoumby.hassan.quran.data.field.SurahVerseField
import com.mfoumby.hassan.quran.data.field.SurahVersesField

data class RemoteSurahVerse(
    @get:PropertyName(SurahVerseField.Remote.SURAH_VERSE_NUMBER)
    @set:PropertyName(SurahVerseField.Remote.SURAH_VERSE_NUMBER)
    var number: Int = 0,
    @get:PropertyName(SurahVerseField.Remote.SURAH_VERSE_SURAH_NUMBER)
    @set:PropertyName(SurahVerseField.Remote.SURAH_VERSE_SURAH_NUMBER)
    var surahNumber: Int = 0,
    @get:PropertyName(SurahVerseField.Remote.SURAH_VERSE_TEXT)
    @set:PropertyName(SurahVerseField.Remote.SURAH_VERSE_TEXT)
    var text: String = ""
)

data class RemoteSurahVerses(
    @get:PropertyName(SurahVersesField.Remote.VALUES)
    @set:PropertyName(SurahVersesField.Remote.VALUES)
    var values: List<RemoteSurahVerse> = emptyList()
)