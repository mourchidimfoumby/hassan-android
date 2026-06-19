package com.mfoumby.hassan.quran.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mfoumby.hassan.common.data.model.LocalTranslationLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class SurahVerseTranslationLanguageDataStore(context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "surah_verse_translation_language")
    private val store = context.dataStore
    private val suraVerseTranslationLanguageKey = stringPreferencesKey("suraVerseTranslationLanguageKey")
    private val gson = Gson()

    fun getTranslationLanguagesFlow(): Flow<List<LocalTranslationLanguage>> = store.data.map {
        it[suraVerseTranslationLanguageKey]?.let { json ->
            val type = object : TypeToken<List<LocalTranslationLanguage>>() {}.type
            gson.fromJson(json, type)
        } ?: emptyList()
    }

    suspend fun getTranslationLanguages(): List<LocalTranslationLanguage> = store.data.firstOrNull()?.get(suraVerseTranslationLanguageKey)?.let {
        val type = object : TypeToken<List<LocalTranslationLanguage>>() {}.type
        gson.fromJson(it, type)
    } ?: emptyList()

    suspend fun setTranslationLanguages(translationLanguages: List<LocalTranslationLanguage>) {
        store.updateData {
            it.toMutablePreferences().apply {
                set(suraVerseTranslationLanguageKey, gson.toJson(translationLanguages))
            }
        }
    }

    suspend fun updateTranslationLanguage(translationLanguage: LocalTranslationLanguage) {
        val translationLanguages = getTranslationLanguages().map {
            if (it.language == translationLanguage.language) translationLanguage else it
        }
        setTranslationLanguages(translationLanguages)
    }
}