package com.mfoumby.hassan.quran.data.repository

import android.util.Log
import com.mfoumby.hassan.quran.data.local.SurahVerseAudioLocalDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseAudioRemoteDataSource
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import com.mfoumby.hassan.quran.domain.repository.SurahVerseAudioRepository

class SurahVerseAudioRepositoryImpl(
    private val surahVerseAudioLocalDataSource: SurahVerseAudioLocalDataSource,
    private val surahVerseAudioRemoteDataSource: SurahVerseAudioRemoteDataSource
): SurahVerseAudioRepository {
    override suspend fun getSurahVerseAudios(surah: Surah, reciterId: String, offset: Int, limit: Int): List<SurahVerseAudio> =
        surahVerseAudioLocalDataSource.getSurahVerseAudios(surah, reciterId, offset, limit)

    override suspend fun deleteSurahVerseAudios(surahNumber: Int, reciterId: String) {
        surahVerseAudioLocalDataSource.deleteSurahVerseAudios(surahNumber, reciterId)
    }

    override suspend fun downloadSurahVerseAudio(surahNumber: Int, verseNumber: Int, reciterId: String) {
        try {
            surahVerseAudioRemoteDataSource.downloadSurahVerseAudio(surahNumber, verseNumber, reciterId).let {
                surahVerseAudioLocalDataSource.storeSurahVerseAudio(surahNumber, verseNumber, reciterId, it)
            }
        } catch (e: Exception) {
            Log.e(
                "SurahVerseAudioRepositoryImpl",
                "The downloading of $reciterId audio recitation failed for surah $surahNumber:$verseNumber : ${e.message}",
                e
            )
            throw e
        }
    }

    override suspend fun isSurahVerseAudioDownloaded(surah: Surah, reciterId: String): Boolean =
        surahVerseAudioLocalDataSource.isSurahVerseAudioDownloaded(surah, reciterId)
}