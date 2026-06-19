package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.common.data.model.RemoteTranslationLanguage

interface SurahVerseTranslationLanguageApi {
    suspend fun getTranslationLanguages(): List<RemoteTranslationLanguage>
}