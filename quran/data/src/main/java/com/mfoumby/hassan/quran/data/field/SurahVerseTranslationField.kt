package com.mfoumby.hassan.quran.data.field

object SurahVerseTranslationsField {
    object Remote {
        const val VALUES = "values"
    }
}

object SurahVerseTranslationField {
    object Local {
        const val TABLE_NAME = "surah_verse_translations"
        const val VERSE_NUMBER = "surah_verse_translation_verse_number"
        const val SURAH_NUMBER = "surah_verse_translation_surah_number"
        const val JUZ_NUMBER = "surah_verse_translation_juz_number"
        const val HIZB_NUMBER = "surah_verse_translation_hizb_number"
        const val TRANSLATION = "surah_verse_translation_translation"
        const val LANGUAGE = "surah_verse_translation_language"
    }

    object Remote {
        const val VERSE_NUMBER = "verseNumber"
        const val SURAH_NUMBER = "surahNumber"
        const val JUZ_NUMBER = "juzNumber"
        const val HIZB_NUMBER = "hizbNumber"
        const val TRANSLATION = "translation"
        const val LANGUAGE = "language"
    }
}