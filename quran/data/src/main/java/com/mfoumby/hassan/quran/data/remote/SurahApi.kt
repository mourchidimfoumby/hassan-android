package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.quran.data.model.RemoteSurah
import com.mfoumby.hassan.quran.data.model.RemoteSurahTranslation

interface SurahApi {
    suspend fun getSurahs(): List<RemoteSurah>

    suspend fun getSurahTranslations(language: String): List<RemoteSurahTranslation>
}