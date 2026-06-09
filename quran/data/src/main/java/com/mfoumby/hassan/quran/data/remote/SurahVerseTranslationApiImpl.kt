package com.mfoumby.hassan.quran.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.mfoumby.hassan.quran.data.model.RemoteSurahVerseTranslation
import com.mfoumby.hassan.quran.data.model.RemoteSurahVerseTranslations
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SurahVerseTranslationApiImpl: SurahVerseTranslationApi {
    private val surahVerseTranslationCollection = Firebase.firestore.collection(SURAH_VERSE_TRANSLATION_COLLECTION)
    companion object {
        private const val SURAH_VERSE_TRANSLATION_COLLECTION = "surah-verse-translations"
        private const val SURAH_VERSE_TRANSLATION_DOCUMENT = "surahs"
    }

    override fun getAllSurahVerseTranslations(language: String): Flow<List<RemoteSurahVerseTranslation>> = callbackFlow {
        val batchSize = 20
        var lastDocument: DocumentSnapshot? = null
        var query = surahVerseTranslationCollection
            .document(language)
            .collection(SURAH_VERSE_TRANSLATION_DOCUMENT)
            .limit(batchSize.toLong())

        while (true) {
            if (lastDocument != null) {
                query = query.startAfter(lastDocument)
            }

            val snapshot = query.get().await()
            if (snapshot.isEmpty) {
                break
            }

            val remoteSurahVerseTranslationList = snapshot.toObjects<RemoteSurahVerseTranslations>().map { it.values }.flatten()
            trySend(remoteSurahVerseTranslationList)
            lastDocument = snapshot.documents.last()
        }

        awaitClose {}
    }

    override suspend fun getSurahVerseTranslations(surahNumber: Int, language: String): List<RemoteSurahVerseTranslation> =
        surahVerseTranslationCollection
            .document(language)
            .collection(SURAH_VERSE_TRANSLATION_DOCUMENT)
            .document(surahNumber.toString())
            .get()
            .await()
            .toObject<RemoteSurahVerseTranslations>()?.values ?: emptyList()
}