package com.mfoumby.hassan.quran.data.field

object SurahVerseTranslationsField {
    object Remote {
        const val VALUES = "values"
    }
}

object SurahVerseTranslationField {
    object Local {
        const val TABLE_NAME = "surah_verse_translations"
        const val VERSE_NUMBER = "surah_verse_translation_number"
        const val SURAH_NUMBER = "surah_verse_translation_surah_number"
        const val LANGUAGE = "surah_verse_translation_language"
        const val TRANSLATION = "surah_verse_translation_translation"
    }

    object Remote {
        const val VERSE_NUMBER = "number"
        const val SURAH_NUMBER = "surahNumber"
        const val TRANSLATION = "translation"
    }
}