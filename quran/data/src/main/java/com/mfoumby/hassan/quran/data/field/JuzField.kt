package com.mfoumby.hassan.quran.data.field

object JuzField {
    object Local {
        const val JUZ_VIEW_NAME = "all_juz"
        const val JUZ_NUMBER = "juz_number"
        const val JUZ_FIRST_VERSE = "first_verse"
        const val JUZ_FIRST_SURAH = "first_surah"

        const val JUZ_FIRST_VERSE_NUMBER = "${JUZ_FIRST_VERSE}_${VerseField.Local.VERSE_NUMBER}"
        const val JUZ_FIRST_VERSE_SURAH_NUMBER = "${JUZ_FIRST_VERSE}_${VerseField.Local.VERSE_SURAH_NUMBER}"
        const val JUZ_FIRST_VERSE_TEXT = "${JUZ_FIRST_VERSE}_${VerseField.Local.VERSE_TEXT}"
        const val JUZ_FIRST_VERSE_PAGE = "${JUZ_FIRST_VERSE}_${VerseField.Local.VERSE_PAGE}"
        const val JUZ_FIRST_VERSE_JUZ = "${JUZ_FIRST_VERSE}_${VerseField.Local.VERSE_JUZ}"

        const val JUZ_FIRST_SURAH_NUMBER = "${JUZ_FIRST_SURAH}_${SurahField.Local.SURAH_NUMBER}"
        const val JUZ_FIRST_SURAH_NAME = "${JUZ_FIRST_SURAH}_${SurahField.Local.SURAH_NAME}"
        const val JUZ_FIRST_SURAH_TRANSLITERATION = "${JUZ_FIRST_SURAH}_${SurahField.Local.SURAH_TRANSLITERATION}"
        const val JUZ_FIRST_SURAH_TOTAL_VERSES = "${JUZ_FIRST_SURAH}_${SurahField.Local.SURAH_TOTAL_VERSES}"
        const val JUZ_FIRST_SURAH_TYPE = "${JUZ_FIRST_SURAH}_${SurahField.Local.SURAH_TYPE}"
        const val JUZ_FIRST_SURAH_TRANSLATION = "${JUZ_FIRST_SURAH}_${SurahField.Local.SURAH_TRANSLATION}"
    }
}