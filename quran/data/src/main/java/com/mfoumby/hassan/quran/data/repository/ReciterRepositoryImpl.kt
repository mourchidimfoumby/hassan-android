package com.mfoumby.hassan.quran.data.repository

import com.mfoumby.hassan.common.data.e
import com.mfoumby.hassan.quran.data.local.ReciterLocalDataSource
import com.mfoumby.hassan.quran.data.remote.ReciterRemoteDataSource
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import com.mfoumby.hassan.quran.domain.repository.ReciterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ReciterRepositoryImpl(
    private val reciterLocalDataSource: ReciterLocalDataSource,
    private val reciterRemoteDataSource: ReciterRemoteDataSource
): ReciterRepository {
    private var lastReciterAudioSubFolder: Pair<String, String>? = null

    override suspend fun getReciters(): List<Reciter> = reciterLocalDataSource.getReciters()

    override suspend fun fetchReciterCount(): Int = reciterRemoteDataSource.getReciterCount()

    override suspend fun getSurahVerseAudios(surahNumber: Int, reciterId: String): List<SurahVerseAudio> =
        reciterLocalDataSource.getSurahVerseAudios(surahNumber, reciterId)

    override suspend fun downloadReciters() {
        reciterRemoteDataSource.getReciters().let {
            reciterLocalDataSource.setReciter(it)
        }
        reciterRemoteDataSource.getReciterAudioSubFolders().let {
            reciterLocalDataSource.setReciterAudioSubFolders(it)
        }
    }

    override suspend fun deleteSurahVerseAudios(surahNumber: Int, reciterId: String) {
        reciterLocalDataSource.deleteSurahVerseAudios(surahNumber, reciterId)
    }

    override fun downloadSurahVerseAudio(surah: Surah, reciterId: String): Flow<Int> = flow {
        val reciterAudioSubFolder = lastReciterAudioSubFolder
            ?.takeIf { it.first == reciterId }?.second
            ?: reciterLocalDataSource.getReciterAudioSubFolder()[reciterId]

        reciterAudioSubFolder?.let { subFolder ->
            lastReciterAudioSubFolder = reciterId to subFolder
            for (verseNumber in 1..surah.totalVerses) {
                val audiosUrl = reciterRemoteDataSource.formatAudioUrl(surah.number, verseNumber, subFolder)
                reciterRemoteDataSource.downloadSurahVerseAudio(audiosUrl).let {
                    reciterLocalDataSource.storeSurahVerseAudio(surah.number, verseNumber, reciterId, it)
                }
                emit(verseNumber)
            }
        }
    }.catch {
        e("Download recitation audio failed ${it.message}")
        throw it
    }
}