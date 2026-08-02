package com.mfoumby.hassan.quran.data.field

object HizbField {
    object Local {
        const val HIZB_VIEW_NAME = "all_hizb"
        const val HIZB_NUMBER = "hizb_number"
        const val HIZB_FIRST_VERSE = "first_verse"
        const val HIZB_FIRST_SURAH = "first_surah"

        const val HIZB_FIRST_VERSE_NUMBER = "${HIZB_FIRST_VERSE}_${VerseField.Local.VERSE_NUMBER}"
        const val HIZB_FIRST_VERSE_SURAH_NUMBER = "${HIZB_FIRST_VERSE}_${VerseField.Local.VERSE_SURAH_NUMBER}"
        const val HIZB_FIRST_VERSE_TEXT = "${HIZB_FIRST_VERSE}_${VerseField.Local.VERSE_TEXT}"
        const val HIZB_FIRST_VERSE_PAGE = "${HIZB_FIRST_VERSE}_${VerseField.Local.VERSE_PAGE}"
        const val HIZB_FIRST_VERSE_HIZB = "${HIZB_FIRST_VERSE}_${VerseField.Local.VERSE_HIZB}"

        const val HIZB_FIRST_SURAH_NUMBER = "${HIZB_FIRST_SURAH}_${SurahField.Local.SURAH_NUMBER}"
        const val HIZB_FIRST_SURAH_NAME = "${HIZB_FIRST_SURAH}_${SurahField.Local.SURAH_NAME}"
        const val HIZB_FIRST_SURAH_TRANSLITERATION = "${HIZB_FIRST_SURAH}_${SurahField.Local.SURAH_TRANSLITERATION}"
        const val HIZB_FIRST_SURAH_TOTAL_VERSES = "${HIZB_FIRST_SURAH}_${SurahField.Local.SURAH_TOTAL_VERSES}"
        const val HIZB_FIRST_SURAH_TYPE = "${HIZB_FIRST_SURAH}_${SurahField.Local.SURAH_TYPE}"
        const val HIZB_FIRST_SURAH_TRANSLATION = "${HIZB_FIRST_SURAH}_${SurahField.Local.SURAH_TRANSLATION}"
    }
}