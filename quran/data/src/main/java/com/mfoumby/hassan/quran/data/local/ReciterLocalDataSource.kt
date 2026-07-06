package com.mfoumby.hassan.quran.data.local

import com.mfoumby.hassan.quran.data.mapper.toLocal
import com.mfoumby.hassan.quran.data.mapper.toReciter
import com.mfoumby.hassan.quran.data.mapper.toSurahVerseAudio
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ReciterLocalDataSource(
    private val reciterDataStore: ReciterDataStore,
    private val reciterFileStorage: ReciterFileStorage
) {
    private val dispatcher = Dispatchers.IO

    suspend fun getReciters(): List<Reciter> = withContext(dispatcher) {
        reciterDataStore.getReciters().map { it.toReciter() }
    }

    suspend fun setReciter(reciters: List<Reciter>) {
        withContext(dispatcher) {
            reciterDataStore.setReciters(reciters.map { it.toLocal() })
        }
    }

    suspend fun getReciterAudioSubFolder(): Map<String, String> = withContext(dispatcher) {
        reciterDataStore.getReciterAudioSubFolders()
    }

    suspend fun setReciterAudioSubFolders(reciterAudioSubfolders: Map<String, String>) {
        withContext(dispatcher) {
            reciterDataStore.setReciterAudioSubFolders(reciterAudioSubfolders)
        }
    }

    suspend fun getSurahVerseAudios(surahNumber: Int, reciterId: String): List<SurahVerseAudio> =
        withContext(dispatcher) {
            reciterFileStorage.getSurahVerseAudios(surahNumber, reciterId).map { it.toSurahVerseAudio() }
        }

    suspend fun storeSurahVerseAudio(surahNumber: Int, verseNumber: Int, reciterId: String, file: File) {
        withContext(dispatcher) {
            reciterFileStorage.storeSurahVerseAudio(surahNumber, verseNumber, reciterId, file)
        }
    }

    suspend fun deleteSurahVerseAudios(surahNumber: Int, reciterId: String) {
        withContext(dispatcher) {
            reciterFileStorage.deleteSurahVerseAudios(surahNumber, reciterId)
        }
    }
}