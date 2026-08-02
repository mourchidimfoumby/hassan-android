package com.mfoumby.hassan.quran.data.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import com.mfoumby.hassan.quran.data.field.JuzField.Local.JUZ_FIRST_SURAH
import com.mfoumby.hassan.quran.data.field.JuzField.Local.JUZ_FIRST_VERSE
import com.mfoumby.hassan.quran.data.field.JuzField.Local.JUZ_NUMBER
import com.mfoumby.hassan.quran.data.field.JuzField.Local.JUZ_VIEW_NAME
import com.mfoumby.hassan.quran.data.field.SurahField
import com.mfoumby.hassan.quran.data.field.SurahField.Local.SURAH_TABLE_NAME
import com.mfoumby.hassan.quran.data.field.VerseField
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_TABLE_NAME

@DatabaseView(
    viewName = JUZ_VIEW_NAME,
    value = """
        SELECT 
            ${VerseField.Local.VERSE_JUZ} AS $JUZ_NUMBER,

            V.${VerseField.Local.VERSE_NUMBER} AS ${JUZ_FIRST_VERSE}_${VerseField.Local.VERSE_NUMBER},
            V.${VerseField.Local.VERSE_SURAH_NUMBER} AS ${JUZ_FIRST_VERSE}_${VerseField.Local.VERSE_SURAH_NUMBER},
            V.${VerseField.Local.VERSE_TEXT} AS ${JUZ_FIRST_VERSE}_${VerseField.Local.VERSE_TEXT},
            V.${VerseField.Local.VERSE_PAGE} AS ${JUZ_FIRST_VERSE}_${VerseField.Local.VERSE_PAGE},
            V.${VerseField.Local.VERSE_JUZ} AS ${JUZ_FIRST_VERSE}_${VerseField.Local.VERSE_JUZ},

            S.${SurahField.Local.SURAH_NUMBER} AS ${JUZ_FIRST_SURAH}_${SurahField.Local.SURAH_NUMBER},
            S.${SurahField.Local.SURAH_NAME} AS ${JUZ_FIRST_SURAH}_${SurahField.Local.SURAH_NAME},
            S.${SurahField.Local.SURAH_TRANSLITERATION} AS ${JUZ_FIRST_SURAH}_${SurahField.Local.SURAH_TRANSLITERATION},
            S.${SurahField.Local.SURAH_TOTAL_VERSES} AS ${JUZ_FIRST_SURAH}_${SurahField.Local.SURAH_TOTAL_VERSES},
            S.${SurahField.Local.SURAH_TYPE} AS ${JUZ_FIRST_SURAH}_${SurahField.Local.SURAH_TYPE},
            S.${SurahField.Local.SURAH_TRANSLATION} AS ${JUZ_FIRST_SURAH}_${SurahField.Local.SURAH_TRANSLATION}
        FROM $VERSE_TABLE_NAME V
        INNER JOIN (
            SELECT ${VerseField.Local.VERSE_JUZ} AS JUZ, MIN(${VerseField.Local.VERSE_SURAH_NUMBER}) AS SURAH_NUMBER
            FROM $VERSE_TABLE_NAME
            GROUP BY ${VerseField.Local.VERSE_JUZ}
        ) FIRST_SURAH
        ON V.${VerseField.Local.VERSE_JUZ} = FIRST_SURAH.JUZ 
        AND V.${VerseField.Local.VERSE_SURAH_NUMBER} = FIRST_SURAH.SURAH_NUMBER
        INNER JOIN $SURAH_TABLE_NAME S ON V.${VerseField.Local.VERSE_SURAH_NUMBER} = S.${SurahField.Local.SURAH_NUMBER}
        WHERE V.${VerseField.Local.VERSE_NUMBER} = (
            SELECT MIN(V2.${VerseField.Local.VERSE_NUMBER})
            FROM $VERSE_TABLE_NAME V2
            WHERE V2.${VerseField.Local.VERSE_JUZ} = V.${VerseField.Local.VERSE_JUZ}
              AND V2.${VerseField.Local.VERSE_SURAH_NUMBER} = V.${VerseField.Local.VERSE_SURAH_NUMBER}
        );
    """
)
data class LocalJuz(
    @ColumnInfo(name = JUZ_NUMBER)
    val number: Int,
    @Embedded(prefix = "${JUZ_FIRST_VERSE}_")
    val firstVerse: LocalVerse,
    @Embedded(prefix = "${JUZ_FIRST_SURAH}_")
    val firstSurah: LocalSurah
)
