package com.mfoumby.hassan.quran.data.local

import android.content.Context
import androidx.core.net.toUri
import com.mfoumby.hassan.quran.data.Constants.AUDIO_FILE_EXTENSION
import com.mfoumby.hassan.quran.data.model.LocalSurahVerseAudio
import com.mfoumby.hassan.quran.domain.entity.Surah
import java.io.File

class SurahVerseAudioFileStorage(private val context: Context) {
    companion object {
        private const val AUDIO_RECITATION_FOLDER = "audio_recitation"
    }

    fun storeSurahVerseAudio(surahNumber: Int, verseNumber: Int, reciterId: String, file: File) {
        val finalFile = File(
            context.filesDir,
            "${audioRecitationPath(surahNumber, reciterId)}/$verseNumber.$AUDIO_FILE_EXTENSION"
        )
        finalFile.parentFile?.let {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
        file.copyTo(finalFile, overwrite = true)
        file.delete()
    }

    fun getSurahVerseAudios(surahNumber: Int, reciterId: String, offset: Int, limit: Int): List<LocalSurahVerseAudio> {
        val folder = File(context.filesDir, audioRecitationPath(surahNumber, reciterId))
        return folder.listFiles(File::isFile)
            ?.map {
                LocalSurahVerseAudio(
                    surahNumber = surahNumber,
                    verseNumber = it.name.removeSuffix(".$AUDIO_FILE_EXTENSION").toInt(),
                    audioUrl = it.toUri().toString()
                )
            }
            ?.sortedBy { it.verseNumber }
            ?.drop(offset)
            ?.take(limit)
            ?: emptyList()
    }

    fun deleteSurahVerseAudios(surahNumber: Int, reciterId: String) {
        val folder = File(context.filesDir, audioRecitationPath(surahNumber, reciterId))
        folder.deleteRecursively()
    }

    fun isSurahVerseAudioDownloaded(surah: Surah, reciterId: String): Boolean {
        val folder = File(context.filesDir, audioRecitationPath(surah.number, reciterId))
        return folder.listFiles()?.count { it.isFile } == surah.totalVerses
    }

    private fun audioRecitationPath(surahNumber: Int, reciterId: String): String =
        "$AUDIO_RECITATION_FOLDER/$reciterId/$surahNumber"
}