package com.mfoumby.hassan.quran.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.mfoumby.hassan.common.data.model.RemoteTranslationLanguage
import com.mfoumby.hassan.quran.data.remote.FirestoreCollectionReferences.SURAH_VERSE_TRANSLATIONS_COLLECTION
import kotlinx.coroutines.tasks.await

class SurahVerseTranslationLanguageApiImpl: SurahVerseTranslationLanguageApi {
    private val surahVerseTranslationCollection = Firebase.firestore.collection(SURAH_VERSE_TRANSLATIONS_COLLECTION)

    override suspend fun getTranslationLanguages(): List<RemoteTranslationLanguage> =
        surahVerseTranslationCollection
            .get()
            .await()
            .toObjects()
}