package com.mfoumby.hassan.quran.data.repository

import com.mfoumby.hassan.common.data.e
import com.mfoumby.hassan.quran.data.local.SurahVerseAudioLocalDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseAudioRemoteDataSource
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import com.mfoumby.hassan.quran.domain.repository.SurahVerseAudioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SurahVerseAudioRepositoryImpl(
    private val surahVerseAudioLocalDataSource: SurahVerseAudioLocalDataSource,
    private val surahVerseAudioRemoteDataSource: SurahVerseAudioRemoteDataSource
): SurahVerseAudioRepository {
    override suspend fun getSurahVerseAudios(surah: Surah, reciterId: String, offset: Int, limit: Int): List<SurahVerseAudio> =
        surahVerseAudioLocalDataSource.getSurahVerseAudios(surah, reciterId, offset, limit)

    override suspend fun deleteSurahVerseAudios(surahNumber: Int, reciterId: String) {
        surahVerseAudioLocalDataSource.deleteSurahVerseAudios(surahNumber, reciterId)
    }

    override fun downloadSurahVerseAudio(surah: Surah, reciterId: String): Flow<Int> = flow {
        for (verseNumber in 1..surah.totalVerses) {
            surahVerseAudioRemoteDataSource.downloadSurahVerseAudio(surah.number, verseNumber, reciterId).let {
                surahVerseAudioLocalDataSource.storeSurahVerseAudio(surah.number, verseNumber, reciterId, it)
            }
            emit(verseNumber)
        }
    }.catch {
        e("The downloading of $reciterId audio recitation failed for surah ${surah.transliteration} : ${it.message}", it)
        throw it
    }

    override suspend fun isSurahVerseAudioDownloaded(surah: Surah, reciterId: String): Boolean =
        surahVerseAudioLocalDataSource.isSurahVerseAudioDownloaded(surah, reciterId)
}