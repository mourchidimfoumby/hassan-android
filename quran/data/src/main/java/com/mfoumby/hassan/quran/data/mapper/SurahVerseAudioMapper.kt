package com.mfoumby.hassan.quran.data.mapper

import com.mfoumby.hassan.quran.data.model.LocalSurahVerseAudio
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio

fun SurahVerseAudio.toLocal() = LocalSurahVerseAudio(
    surahNumber = surah.number,
    verseNumber = verseNumber,
    audioUrl = audioUri
)

fun LocalSurahVerseAudio.toSurahVerseAudio(surah: Surah) = SurahVerseAudio(
    surah = surah,
    verseNumber = verseNumber,
    audioUri = audioUrl
)