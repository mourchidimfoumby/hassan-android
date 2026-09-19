package com.mfoumby.hassan.quran.domain.usecase

import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.repository.SurahVerseAudioRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.incrementAndFetch

class DownloadSurahVerseAudioUseCase(
    private val surahVerseAudioRepository: SurahVerseAudioRepository
) {
    companion object {
        private const val CONCURRENCY = 10
    }

    @OptIn(ExperimentalAtomicApi::class, ExperimentalCoroutinesApi::class)
    fun execute(surah: Surah, reciterId: String): Flow<Int> {
        val progress = AtomicInt(0)

        return IntRange(1, surah.totalVerses)
            .asFlow()
            .flatMapMerge(CONCURRENCY) { verseNumber ->
                flow {
                    surahVerseAudioRepository.downloadSurahVerseAudio(
                        surah.number,
                        verseNumber,
                        reciterId
                    )
                    emit(progress.incrementAndFetch())
                }
            }
    }
}