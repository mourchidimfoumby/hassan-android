package com.mfoumby.hassan.quran.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mfoumby.hassan.common.data.model.LocalTranslationLanguage
import com.mfoumby.hassan.quran.data.extension.getJson
import com.mfoumby.hassan.quran.data.extension.getJsonFlow
import com.mfoumby.hassan.quran.data.extension.setJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SurahVerseTranslationLanguageDataStore(context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("surah_verse_translation_language")
    private val store = context.dataStore
    private val suraVerseTranslationLanguageKey = stringPreferencesKey("suraVerseTranslationLanguageKey")

    fun getTranslationLanguagesFlow(): Flow<List<LocalTranslationLanguage>> =
        store.getJsonFlow<List<LocalTranslationLanguage>>(suraVerseTranslationLanguageKey).map { it ?: emptyList() }

    suspend fun getTranslationLanguages(): List<LocalTranslationLanguage> = store.getJson(suraVerseTranslationLanguageKey) ?: emptyList()

    suspend fun setTranslationLanguages(translationLanguages: List<LocalTranslationLanguage>) {
        store.setJson(suraVerseTranslationLanguageKey, translationLanguages)
    }

    suspend fun updateTranslationLanguage(translationLanguage: LocalTranslationLanguage) {
        val translationLanguages = getTranslationLanguages().map {
            if (it.language == translationLanguage.language) translationLanguage else it
        }
        setTranslationLanguages(translationLanguages)
    }
}