package com.mfoumby.hassan.quran.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.mfoumby.hassan.quran.data.model.RemoteSurahVerseTranslation
import com.mfoumby.hassan.quran.data.model.RemoteSurahVerseTranslations
import com.mfoumby.hassan.quran.data.remote.FirestoreCollectionReferences.SURAH_VERSE_TRANSLATIONS_COLLECTION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class SurahVerseTranslationApiImpl: SurahVerseTranslationApi {
    private val surahVerseTranslationCollection = Firebase.firestore.collection(SURAH_VERSE_TRANSLATIONS_COLLECTION)
    companion object {
        private const val SURAH_VERSE_TRANSLATION_DOCUMENT = "surahs"
    }

    override fun getAllSurahVerseTranslations(language: String): Flow<List<RemoteSurahVerseTranslation>> = flow {
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
            emit(remoteSurahVerseTranslationList)
            lastDocument = snapshot.documents.last()
        }
    }
}