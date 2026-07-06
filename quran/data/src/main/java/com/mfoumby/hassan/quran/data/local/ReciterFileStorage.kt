package com.mfoumby.hassan.quran.data.local

import android.content.Context
import androidx.core.net.toUri
import com.mfoumby.hassan.quran.data.Constants.AUDIO_FILE_EXTENSION
import com.mfoumby.hassan.quran.data.model.LocalSurahVerseAudio
import java.io.File

class ReciterFileStorage(private val context: Context) {
    companion object {
        private const val RECITATION_AUDIO_FOLDER = "recitation_audio"
    }

    fun storeSurahVerseAudio(surahNumber: Int, verseNumber: Int, reciterId: String, file: File) {
        val finalFile = File(
            context.filesDir,
            "${getRecitationAudioPath(surahNumber, reciterId)}/$verseNumber.$AUDIO_FILE_EXTENSION"
        )
        finalFile.parentFile?.let {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
        file.copyTo(finalFile, overwrite = true)
        file.delete()
    }

    fun getSurahVerseAudios(surahNumber: Int, reciterId: String): List<LocalSurahVerseAudio> {
        val folder = File(context.filesDir, getRecitationAudioPath(surahNumber, reciterId))
        return folder.listFiles(File::isFile)?.map {
            LocalSurahVerseAudio(
                verseNumber = it.name.removeSuffix(".$AUDIO_FILE_EXTENSION").toInt(),
                audioUrl = it.toUri().toString()
            )
        } ?: emptyList()
    }

    fun deleteSurahVerseAudios(surahNumber: Int, reciterId: String) {
        val folder = File(context.filesDir, getRecitationAudioPath(surahNumber, reciterId))
        folder.deleteRecursively()
    }


    private fun getRecitationAudioPath(surahNumber: Int, reciterId: String): String =
        "$RECITATION_AUDIO_FOLDER/$reciterId/$surahNumber"
}