package com.mfoumby.hassan.quran.ui.surahverse.tajweed

object TajweedUtils {
    private val patterns = listOf(
        TajweedPattern.ghunnahPattern.toRegex() to Tajweed.TajweedType.GHUNNAH,
        TajweedPattern.ikhfaaPattern.toRegex() to Tajweed.TajweedType.IKHFAA,
        TajweedPattern.iqlaabPattern.toRegex() to Tajweed.TajweedType.IQLAAB,
        TajweedPattern.idghaamWithGhunnahPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_WITH_GHUNNAH_NUN_SAKINAH,
        TajweedPattern.idghaamWithoutGhunnahPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_WITHOUT_GHUNNAH,
        TajweedPattern.idghaamShafawiPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_SHAFAWI,
        TajweedPattern.qalqalahPattern.toRegex() to Tajweed.TajweedType.QALQALAH,
        TajweedPattern.maddFourTimePattern.toRegex() to Tajweed.TajweedType.MADD_FOUR_TIME,
        TajweedPattern.maddSixTimePattern.toRegex() to Tajweed.TajweedType.MADD_SIX_TIME,
        TajweedPattern.maddHarfMuqattatPattern.toRegex() to Tajweed.TajweedType.MADD_HARF_MUQATTAT,
        TajweedPattern.maddSuperscriptAlif.toRegex() to Tajweed.TajweedType.MADD_SUPERSCRIPT_ALIF,
    )

    fun getTajweed(verse: String): List<Tajweed> {
        return patterns.flatMap { (regex, type) ->
            regex.findAll(verse).map { r ->
                val endOffset = when (type) {
                    Tajweed.TajweedType.MADD_SIX_TIME -> -1
                    else -> 1
                }

                Tajweed(
                    type,
                    r.value,
                    r.range.first,
                    r.range.last + endOffset
                )
            }
        }
    }
}