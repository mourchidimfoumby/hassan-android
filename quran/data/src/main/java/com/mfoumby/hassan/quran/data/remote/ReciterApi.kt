package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.quran.data.model.RemoteReciter
import java.io.File

interface ReciterApi {
    suspend fun getReciters(): List<RemoteReciter>

    suspend fun getReciterAudioSubFolders(): Map<String, String>

    suspend fun getReciterCount(): Long

    suspend fun downloadAudioRecitation(url: String): File

    fun formatAudioUrl(surahNumber: Int, verseNumber: Int, reciterSubFolder: String): String
}