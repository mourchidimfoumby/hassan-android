package com.mfoumby.hassan.quran.data.field

object VerseField {
    object Local {
        const val VERSE_TABLE_NAME = "verses"
        const val VERSE_NUMBER = "verse_number"
        const val VERSE_SURAH_NUMBER = "verse_surah_number"
        const val VERSE_TEXT = "verse_text"
        const val VERSE_PAGE = "verse_page"
        const val VERSE_JUZ = "verse_juz"
    }

    object Remote {
        const val VERSE_NUMBER = "number"
        const val VERSE_SURAH_NUMBER = "surahNumber"
        const val VERSE_TEXT = "text"
        const val VERSE_PAGE = "page"
        const val VERSE_JUZ = "juz"
    }
}

object VersesField {
    object Remote {
        const val VALUES = "values"
    }
}