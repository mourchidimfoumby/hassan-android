package com.mfoumby.hassan.quran.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.mfoumby.hassan.quran.data.model.RemoteVerse
import com.mfoumby.hassan.quran.data.model.RemoteVerses
import com.mfoumby.hassan.quran.data.remote.FirestoreCollectionReferences.SURAH_VERSES_COLLECTION
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class VerseApiImpl: VerseApi {
    private val surahVerseCollection = Firebase.firestore.collection(SURAH_VERSES_COLLECTION)

    override fun getAllVerses(): Flow<List<RemoteVerse>> = callbackFlow {
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

            val remoteSurahVerseList = snapshot.toObjects<RemoteVerses>().map { it.values }.flatten()
            trySend(remoteSurahVerseList)
            lastDocument = snapshot.documents.last()
        }

        awaitClose {}
    }

    override suspend fun getVerses(surahNumber: Int): List<RemoteVerse> =
        surahVerseCollection.document(surahNumber.toString())
            .get()
            .await()
            .toObject<RemoteVerses>()?.values ?: emptyList()
}