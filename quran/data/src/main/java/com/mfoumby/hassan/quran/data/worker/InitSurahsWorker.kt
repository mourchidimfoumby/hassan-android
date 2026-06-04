package com.mfoumby.hassan.quran.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mfoumby.hassan.common.domain.Language
import com.mfoumby.hassan.quran.domain.SurahRepository
import org.koin.java.KoinJavaComponent.inject
import java.util.Locale

class InitSurahsWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    private val surahRepository: SurahRepository by inject(SurahRepository::class.java)

    override suspend fun doWork(): Result {
        return try {
            if (surahRepository.getLocalSurahs().isEmpty()) {
                val language = Locale.getDefault().language.toString().let(Language::parseLanguage)
                val remoteSurahs = surahRepository.getRemoteSurahs(language)
                surahRepository.upsertSurahs(remoteSurahs)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}