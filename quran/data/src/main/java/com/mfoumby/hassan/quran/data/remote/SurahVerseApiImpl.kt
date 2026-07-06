package com.mfoumby.hassan.quran.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.mfoumby.hassan.quran.data.model.RemoteSurahVerse
import com.mfoumby.hassan.quran.data.model.RemoteSurahVerses
import com.mfoumby.hassan.quran.data.remote.FirestoreCollectionReferences.SURAH_VERSE_COLLECTION
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SurahVerseApiImpl: SurahVerseApi {
    private val surahVerseCollection = Firebase.firestore.collection(SURAH_VERSE_COLLECTION)

    override fun getAllSurahVerses(): Flow<List<RemoteSurahVerse>> = callbackFlow {
        val batchSize = 20
        var lastDocument: DocumentSnapshot? = null
        var query = surahVerseCollection.limit(batchSize.toLong())

        while (true) {
            if (lastDocument != null) {
                query = query.startAfter(lastDocument)
            }

            val snapshot = query.get().await()
            if (snapshot.isEmpty) {
                break
            }

            val remoteSurahVerseList = snapshot.toObjects<RemoteSurahVerses>().map { it.values }.flatten()
            trySend(remoteSurahVerseList)
            lastDocument = snapshot.documents.last()
        }

        awaitClose {}
    }

    override suspend fun getSurahVerses(surahNumber: Int): List<RemoteSurahVerse> =
        surahVerseCollection.document(surahNumber.toString())
            .get()
            .await()
            .toObject<RemoteSurahVerses>()?.values ?: emptyList()
}