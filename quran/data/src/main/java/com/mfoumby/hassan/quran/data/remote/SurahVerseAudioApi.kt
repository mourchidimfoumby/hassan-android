package com.mfoumby.hassan.quran.data.remote

import java.io.File

interface SurahVerseAudioApi {
    suspend fun downloadAudioRecitation(surahNumber: Int, verseNumber: Int, reciterId: String): File
}