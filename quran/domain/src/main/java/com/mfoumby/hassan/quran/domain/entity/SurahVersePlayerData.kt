package com.mfoumby.hassan.quran.domain.entity

import com.mfoumby.hassan.quran.domain.SurahNumber
import com.mfoumby.hassan.quran.domain.VerseNumber

data class SurahVersePlayerData(
    val reciter: Reciter,
    val surahVerseAudios: Map<Pair<SurahNumber, VerseNumber>, SurahVerseAudio>,
    val state: SurahVersePlayerData.State
) {
    sealed class State {
        data object Idle : SurahVersePlayerData.State()
        data class Playing(val surahVerseAudio: SurahVerseAudio) : SurahVersePlayerData.State()
    }
}