package com.mfoumby.hassan.quran.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mfoumby.hassan.quran.data.extension.getJson
import com.mfoumby.hassan.quran.data.extension.setJson
import com.mfoumby.hassan.quran.data.model.LocalReciter

class ReciterDataStore(context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "audio_recitation")
    private val store = context.dataStore
    private val reciterKey = stringPreferencesKey("reciterKey")
    private val reciterAudioSubfolderKey = stringPreferencesKey("reciterAudioSubfolderKey")

    suspend fun getReciters(): List<LocalReciter> = store.getJson(reciterKey) ?: emptyList()

    suspend fun getReciterAudioSubFolders(): Map<String, String> =
        store.getJson<Map<String, String>>(reciterAudioSubfolderKey) ?: emptyMap()

    suspend fun setReciters(reciters: List<LocalReciter>) {
        store.setJson(reciterKey, reciters)
    }

    suspend fun setReciterAudioSubFolders(reciterAudioSubfolders: Map<String, String>) {
        store.setJson(reciterAudioSubfolderKey, reciterAudioSubfolders)
    }
}