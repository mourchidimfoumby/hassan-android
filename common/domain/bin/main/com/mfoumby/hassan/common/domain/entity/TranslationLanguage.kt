package com.mfoumby.hassan.common.domain.entity

data class TranslationLanguage(
    val language: Language,
    val state: TranslationLanguageState
) {
    sealed class TranslationLanguageState {
        data object NotDownloaded: TranslationLanguageState() {
            override fun toString(): String = TYPE
            const val TYPE = "NOT_DOWNLOADED"
        }

        data class Downloading(val progress: Float): TranslationLanguageState() {
            override fun toString(): String = TYPE
            companion object {
                const val TYPE = "DOWNLOADING"
            }
        }

        data object Downloaded: TranslationLanguageState() {
            override fun toString(): String = TYPE
            const val TYPE = "DOWNLOADED"
        }
    }
}
