package com.mfoumby.hassan.quran.ui.surahverse.tajweed

object TajweedUtils {
    private val patterns = listOf(
        TajweedPattern.ikhfaaPattern.toRegex() to Tajweed.TajweedType.IKHFAA,
        TajweedPattern.idghaamWithGhunnahTanweenPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_WITH_GHUNNAH_TANWEEN,
        TajweedPattern.idghaamWithGhunnahNunSakinahPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_WITH_GHUNNAH_NUN_SAKINAH,
        TajweedPattern.idghaamWithoutGhunnahNunSakinahPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_WITHOUT_GHUNNAH_NUN_SAKINAH,
        TajweedPattern.idghaamWithoutGhunnahTanweenPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_WITHOUT_GHUNNAH_TANWEEN,
        TajweedPattern.qalqalahPattern.toRegex() to Tajweed.TajweedType.QALQALAH,
        TajweedPattern.maddFourTimePattern.toRegex() to Tajweed.TajweedType.MADD_FOUR_TIME,
        TajweedPattern.maddSixTimePattern.toRegex() to Tajweed.TajweedType.MADD_SIX_TIME,
        TajweedPattern.maddHarfMuqattatPattern.toRegex() to Tajweed.TajweedType.MADD_HARF_MUQATTAT,
        TajweedPattern.maddSuperscriptAlif.toRegex() to Tajweed.TajweedType.MADD_SUPERSCRIPT_ALIF
    )

    fun getTajweed(verse: String): List<Tajweed> {
        return patterns.flatMap { (regex, type) ->
            regex.findAll(verse).map { r ->
                val startOffset = when (type) {
                    Tajweed.TajweedType.IDGHAAM_WITH_GHUNNAH_TANWEEN -> 1
                    Tajweed.TajweedType.IDGHAAM_WITHOUT_GHUNNAH_TANWEEN -> 1
                    Tajweed.TajweedType.MADD_SUPERSCRIPT_ALIF -> 3
                    else -> 0
                }

                val endOffset = when (type) {
                    Tajweed.TajweedType.MADD_SIX_TIME -> -1
                    else -> 1
                }

                Tajweed(
                    type,
                    r.value,
                    r.range.first - startOffset,
                    r.range.last + endOffset
                )
            }
        }
    }
}