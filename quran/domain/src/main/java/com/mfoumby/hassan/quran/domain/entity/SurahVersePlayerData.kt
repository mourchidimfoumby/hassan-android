package com.mfoumby.hassan.quran.domain.entity

data class SurahVersePlayerData(
    val reciter: Reciter,
    val surahVerseAudios: List<SurahVerseAudio>,
    val state: SurahVersePlayerData.State
) {
    sealed class State {
        data object Idle : SurahVersePlayerData.State()
        data class Playing(val surahVerseAudio: SurahVerseAudio) : SurahVersePlayerData.State()
    }
}