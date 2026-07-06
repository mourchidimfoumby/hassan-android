package com.mfoumby.hassan.quran.data.mapper

import com.mfoumby.hassan.quran.data.model.LocalSurahVerseAudio
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio

fun SurahVerseAudio.toLocal() = LocalSurahVerseAudio(
    verseNumber = verseNumber,
    audioUrl = audioUri
)

fun LocalSurahVerseAudio.toSurahVerseAudio() = SurahVerseAudio(
    verseNumber = verseNumber,
    audioUri = audioUrl
)