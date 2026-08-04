package com.mfoumby.hassan.quran.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.mfoumby.hassan.quran.data.Constants.AUDIO_FILE_EXTENSION
import com.mfoumby.hassan.quran.data.model.RemoteSurahVerseAudio
import com.mfoumby.hassan.quran.data.remote.FirestoreCollectionReferences.SURAH_VERSE_AUDIOS_COLLECTION
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SurahVerseAudioApiImpl: SurahVerseAudioApi {
    private val surahVerseAudioCollection = Firebase.firestore.collection(SURAH_VERSE_AUDIOS_COLLECTION)

    companion object {
        private const val AUDIO_BASE_URL: String = "https://everyayah.com/data"
    }

    override suspend fun downloadAudioRecitation(surahNumber: Int, verseNumber: Int, reciterId: String): File {
        val audioSubFolder = getSurahVerseAudio(reciterId)?.audioSubFolder ?: throw NullPointerException()
        val url = audioUrl(surahNumber, verseNumber, audioSubFolder)
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val tempFile = File.createTempFile("audio_recitation_${surahNumber}_$verseNumber", ".$AUDIO_FILE_EXTENSION")

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

    private suspend fun getSurahVerseAudio(reciterId: String): RemoteSurahVerseAudio? =
        surahVerseAudioCollection
            .document(reciterId)
            .get()
            .await()
            .toObject<RemoteSurahVerseAudio>()

    private fun audioUrl(surahNumber: Int, verseNumber: Int, reciterSubFolder: String): String =
        "$AUDIO_BASE_URL/$reciterSubFolder/${surahNumber.zeroPad(3)}${verseNumber.zeroPad(3)}.$AUDIO_FILE_EXTENSION"

    private fun Int.zeroPad(occurrence: Int): String = toString().padStart(occurrence, '0')
}