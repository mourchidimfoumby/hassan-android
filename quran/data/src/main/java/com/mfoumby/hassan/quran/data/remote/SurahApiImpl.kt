package com.mfoumby.hassan.quran.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.mfoumby.hassan.quran.data.model.RemoteSurah
import com.mfoumby.hassan.quran.data.model.RemoteSurahTranslation
import com.mfoumby.hassan.quran.data.model.RemoteSurahTranslations
import kotlinx.coroutines.tasks.await

class SurahApiImpl: SurahApi {
    private val surahCollection = Firebase.firestore.collection("surahs")
    private val surahTranslationCollection = Firebase.firestore.collection("surah-translations")

    override suspend fun getSurahs(): List<RemoteSurah> = surahCollection.get().await().toObjects<RemoteSurah>()

    override suspend fun getSurahTranslations(language: String): List<RemoteSurahTranslation> =
        surahTranslationCollection.document(language)
            .get()
            .await()
            .toObject<RemoteSurahTranslations>()?.values ?: emptyList()
}