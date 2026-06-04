package com.mfoumby.hassan.quran.data

internal object SurahField {
    object Local {
        const val SURAH_TABLE_NAME = "surahs"
        const val SURAH_NUMBER = "surah_number"
        const val SURAH_NAME = "surah_name"
        const val SURAH_TRANSLITERATION = "surah_transliteration"
        const val SURAH_TOTAL_VERSES= "surah_total_verses"
        const val SURAH_TYPE= "surah_type"
        const val SURAH_TRANSLATION = "surah_translation"
    }

    object Remote {
        const val SURAH_NUMBER = "number"
        const val SURAH_NAME = "name"
        const val SURAH_TRANSLITERATION = "transliteration"
        const val SURAH_TOTAL_VERSES= "totalVerses"
        const val SURAH_TYPE= "type"
    }
}