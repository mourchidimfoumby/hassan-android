package com.mfoumby.hassan.quran.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.mfoumby.hassan.quran.data.Constants.AUDIO_FILE_EXTENSION
import com.mfoumby.hassan.quran.data.model.RemoteReciter
import com.mfoumby.hassan.quran.data.remote.FirestoreCollectionReferences.RECITERS_COLLECTION
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ReciterApiImpl: ReciterApi {
    private val reciterCollection = Firebase.firestore.collection(RECITERS_COLLECTION)

    companion object {
        private const val AUDIO_BASE_URL: String = "https://everyayah.com/data"
    }

    override suspend fun getReciters(): List<RemoteReciter> =
        reciterCollection
            .get()
            .await()
            .toObjects<RemoteReciter>()

    override suspend fun getReciterAudioSubFolders(): Map<String, String> =
        getReciters().associate { it.reciterId to it.audioSubFolder }

    override suspend fun getReciterCount(): Long =
        reciterCollection
            .count()
            .get(AggregateSource.SERVER)
            .await().count

    override suspend fun downloadAudioRecitation(url: String): File {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val tempFile = File.createTempFile("audio_recitation_", ".$AUDIO_FILE_EXTENSION")
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("HTTP error $response")
            response.body.byteStream().use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
        }
        return tempFile
    }

    override fun formatAudioUrl(surahNumber: Int, verseNumber: Int, reciterSubFolder: String): String =
        "$AUDIO_BASE_URL/$reciterSubFolder/${surahNumber.zeroPad(3)}${verseNumber.zeroPad(3)}.$AUDIO_FILE_EXTENSION"

    private fun Int.zeroPad(occurrence: Int): String = toString().padStart(occurrence, '0')
}