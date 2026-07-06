package com.mfoumby.hassan.quran.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.mfoumby.hassan.quran.data.model.RemoteSurah
import com.mfoumby.hassan.quran.data.model.RemoteSurahTranslation
import com.mfoumby.hassan.quran.data.model.RemoteSurahTranslations
import com.mfoumby.hassan.quran.data.remote.FirestoreCollectionReferences.SURAH_COLLECTION
import com.mfoumby.hassan.quran.data.remote.FirestoreCollectionReferences.SURAH_TRANSLATION_COLLECTION
import kotlinx.coroutines.tasks.await

class SurahApiImpl: SurahApi {
    private val surahCollection = Firebase.firestore.collection(SURAH_COLLECTION)
    private val surahTranslationCollection = Firebase.firestore.collection(SURAH_TRANSLATION_COLLECTION)

    override suspend fun getSurahs(): List<RemoteSurah> = surahCollection.get().await().toObjects<RemoteSurah>()

    override suspend fun getSurahTranslations(language: String): List<RemoteSurahTranslation> =
        surahTranslationCollection.document(language)
            .get()
            .await()
            .toObject<RemoteSurahTranslations>()?.values ?: emptyList()
}