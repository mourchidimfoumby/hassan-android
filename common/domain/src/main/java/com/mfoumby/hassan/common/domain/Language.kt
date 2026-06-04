package com.mfoumby.hassan.common.domain

enum class Language {
    ENGLISH,
    FRENCH;

    companion object {
        fun parseLanguage(language: String): Language = when (language) {
            "en" -> ENGLISH
            "fr" -> FRENCH
            else -> ENGLISH
        }
    }
}