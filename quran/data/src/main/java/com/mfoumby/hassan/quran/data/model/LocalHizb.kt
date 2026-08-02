package com.mfoumby.hassan.quran.data.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import com.mfoumby.hassan.quran.data.field.HizbField.Local.HIZB_FIRST_SURAH
import com.mfoumby.hassan.quran.data.field.HizbField.Local.HIZB_FIRST_VERSE
import com.mfoumby.hassan.quran.data.field.HizbField.Local.HIZB_NUMBER
import com.mfoumby.hassan.quran.data.field.HizbField.Local.HIZB_VIEW_NAME
import com.mfoumby.hassan.quran.data.field.SurahField
import com.mfoumby.hassan.quran.data.field.SurahField.Local.SURAH_TABLE_NAME
import com.mfoumby.hassan.quran.data.field.VerseField
import com.mfoumby.hassan.quran.data.field.VerseField.Local.VERSE_TABLE_NAME

@DatabaseView(
    viewName = HIZB_VIEW_NAME,
    value = """
        SELECT 
            ${VerseField.Local.VERSE_HIZB} AS $HIZB_NUMBER,

            V.${VerseField.Local.VERSE_NUMBER} AS ${HIZB_FIRST_VERSE}_${VerseField.Local.VERSE_NUMBER},
            V.${VerseField.Local.VERSE_SURAH_NUMBER} AS ${HIZB_FIRST_VERSE}_${VerseField.Local.VERSE_SURAH_NUMBER},
            V.${VerseField.Local.VERSE_TEXT} AS ${HIZB_FIRST_VERSE}_${VerseField.Local.VERSE_TEXT},
            V.${VerseField.Local.VERSE_PAGE} AS ${HIZB_FIRST_VERSE}_${VerseField.Local.VERSE_PAGE},
            V.${VerseField.Local.VERSE_JUZ} AS ${HIZB_FIRST_VERSE}_${VerseField.Local.VERSE_JUZ},
            V.${VerseField.Local.VERSE_HIZB} AS ${HIZB_FIRST_VERSE}_${VerseField.Local.VERSE_HIZB},

            S.${SurahField.Local.SURAH_NUMBER} AS ${HIZB_FIRST_SURAH}_${SurahField.Local.SURAH_NUMBER},
            S.${SurahField.Local.SURAH_NAME} AS ${HIZB_FIRST_SURAH}_${SurahField.Local.SURAH_NAME},
            S.${SurahField.Local.SURAH_TRANSLITERATION} AS ${HIZB_FIRST_SURAH}_${SurahField.Local.SURAH_TRANSLITERATION},
            S.${SurahField.Local.SURAH_TOTAL_VERSES} AS ${HIZB_FIRST_SURAH}_${SurahField.Local.SURAH_TOTAL_VERSES},
            S.${SurahField.Local.SURAH_TYPE} AS ${HIZB_FIRST_SURAH}_${SurahField.Local.SURAH_TYPE},
            S.${SurahField.Local.SURAH_TRANSLATION} AS ${HIZB_FIRST_SURAH}_${SurahField.Local.SURAH_TRANSLATION}
        FROM $VERSE_TABLE_NAME V
        INNER JOIN (
            SELECT ${VerseField.Local.VERSE_HIZB} AS HIZB, MIN(${VerseField.Local.VERSE_SURAH_NUMBER}) AS SURAH_NUMBER
            FROM $VERSE_TABLE_NAME
            GROUP BY ${VerseField.Local.VERSE_HIZB}
        ) FIRST_SURAH
        ON V.${VerseField.Local.VERSE_HIZB} = FIRST_SURAH.HIZB 
        AND V.${VerseField.Local.VERSE_SURAH_NUMBER} = FIRST_SURAH.SURAH_NUMBER
        INNER JOIN $SURAH_TABLE_NAME S ON V.${VerseField.Local.VERSE_SURAH_NUMBER} = S.${SurahField.Local.SURAH_NUMBER}
        WHERE V.${VerseField.Local.VERSE_NUMBER} = (
            SELECT MIN(V2.${VerseField.Local.VERSE_NUMBER})
            FROM $VERSE_TABLE_NAME V2
            WHERE V2.${VerseField.Local.VERSE_HIZB} = V.${VerseField.Local.VERSE_HIZB}
              AND V2.${VerseField.Local.VERSE_SURAH_NUMBER} = V.${VerseField.Local.VERSE_SURAH_NUMBER}
        );
    """
)
data class LocalHizb(
    @ColumnInfo(name = HIZB_NUMBER)
    val number: Int,
    @Embedded(prefix = "${HIZB_FIRST_VERSE}_")
    val firstVerse: LocalVerse,
    @Embedded(prefix = "${HIZB_FIRST_SURAH}_")
    val firstSurah: LocalSurah
)
