package com.mfoumby.hassan.quran.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.mfoumby.hassan.common.data.model.RemoteTranslationLanguage
import kotlinx.coroutines.tasks.await

class SurahVerseTranslationLanguageApiImpl: SurahVerseTranslationLanguageApi {
    private val surahVerseTranslationCollection = Firebase.firestore.collection(SURAH_VERSE_TRANSLATION_COLLECTION)
    companion object {
        private const val SURAH_VERSE_TRANSLATION_COLLECTION = "surah-verse-translations"
    }

    override suspend fun getTranslationLanguages(): List<RemoteTranslationLanguage> =
        surahVerseTranslationCollection
            .get()
            .await()
            .toObjects()
}