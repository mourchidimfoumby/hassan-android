package com.mfoumby.hassan.quran.data.field

object SurahVerseField {
    object Local {
        const val SURAH_VERSE_TABLE_NAME = "surah_verses"
        const val SURAH_VERSE_NUMBER = "surah_verse_number"
        const val SURAH_VERSE_SURAH_NUMBER = "surah_verse_surah_number"
        const val SURAH_VERSE_TEXT = "surah_verse_text"
    }

    object Remote {
        const val SURAH_VERSE_NUMBER = "number"
        const val SURAH_VERSE_SURAH_NUMBER = "surahNumber"
        const val SURAH_VERSE_TEXT = "text"
    }
}

object SurahVersesField {
    object Remote {
        const val VALUES = "values"
    }
}