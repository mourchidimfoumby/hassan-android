package com.mfoumby.hassan.common.domain.entity

enum class Language {
    ENGLISH,
    FRENCH,
    ARABIC;

    companion object {
        fun fromLanguageCode(language: String): Language = when (language) {
            "en" -> ENGLISH
            "fr" -> FRENCH
            "ar" -> ARABIC
            else -> ENGLISH
        }
    }

    val code: String
        get() = when (this) {
            ENGLISH -> "en"
            FRENCH -> "fr"
            ARABIC -> "ar"
        }
}