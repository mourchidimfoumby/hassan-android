package com.mfoumby.hassan.quran.domain.entity

import com.mfoumby.hassan.quran.domain.SurahNumber
import com.mfoumby.hassan.quran.domain.VerseNumber

data class SurahVersePlayerManifest(
    val reciter: Reciter,
    val surahVerseAudios: Map<Pair<SurahNumber, VerseNumber>, SurahVerseAudio>,
    val state: SurahVersePlayerManifest.State
) {
    sealed class State {
        data object Idle : SurahVersePlayerManifest.State()
        data class Playing(val surahVerseAudio: SurahVerseAudio) : SurahVersePlayerManifest.State()
    }
}