package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.quran.data.mapper.toReciter
import com.mfoumby.hassan.quran.domain.entity.Reciter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ReciterRemoteDataSource(private val reciterApi: ReciterApi) {
    private val dispatcher = Dispatchers.IO

    suspend fun getReciters(): List<Reciter> = withContext(dispatcher) {
        reciterApi.getReciters().map { it.toReciter() }
    }

    suspend fun getReciterAudioSubFolders(): Map<String, String> = withContext(dispatcher) {
        reciterApi.getReciterAudioSubFolders()
    }

    suspend fun getReciterCount(): Int = withContext(dispatcher) {
        reciterApi.getReciterCount().toInt()
    }

    suspend fun downloadSurahVerseAudio(url: String): File = withContext(dispatcher) {
        reciterApi.downloadAudioRecitation(url)
    }

    fun formatAudioUrl(surahNumber: Int, verseNumber: Int, reciterSubFolder: String): String =
        reciterApi.formatAudioUrl(surahNumber, verseNumber, reciterSubFolder)
}