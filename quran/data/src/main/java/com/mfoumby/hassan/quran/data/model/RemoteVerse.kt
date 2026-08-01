package com.mfoumby.hassan.quran.data.model

import com.google.firebase.firestore.PropertyName
import com.mfoumby.hassan.quran.data.field.VerseField
import com.mfoumby.hassan.quran.data.field.VersesField

data class RemoteVerse(
    @get:PropertyName(VerseField.Remote.VERSE_NUMBER)
    @set:PropertyName(VerseField.Remote.VERSE_NUMBER)
    var number: Int = 0,
    @get:PropertyName(VerseField.Remote.VERSE_SURAH_NUMBER)
    @set:PropertyName(VerseField.Remote.VERSE_SURAH_NUMBER)
    var surahNumber: Int = 0,
    @get:PropertyName(VerseField.Remote.VERSE_TEXT)
    @set:PropertyName(VerseField.Remote.VERSE_TEXT)
    var text: String = "",
    @get:PropertyName(VerseField.Remote.VERSE_PAGE)
    @set:PropertyName(VerseField.Remote.VERSE_PAGE)
    var page: Int = 0,
    @get:PropertyName(VerseField.Remote.VERSE_JUZ)
    @set:PropertyName(VerseField.Remote.VERSE_JUZ)
    var juz: Int = 0
)

data class RemoteVerses(
    @get:PropertyName(VersesField.Remote.VALUES)
    @set:PropertyName(VersesField.Remote.VALUES)
    var values: List<RemoteVerse> = emptyList()
)