package com.mfoumby.hassan.quran.ui.surahverse.tajweed

object TajweedUtils {
    private val patterns = listOf(
        TajweedPattern.wazibGhunnahPattern.toRegex() to Tajweed.TajweedType.WAZIB_GHUNNAH,
        TajweedPattern.ikhfaaPattern.toRegex() to Tajweed.TajweedType.IKHFAA,
        TajweedPattern.iqlaabPattern.toRegex() to Tajweed.TajweedType.IQLAAB,
        TajweedPattern.qalqalahAtMiddlePattern.toRegex() to Tajweed.TajweedType.QALQALAH_AT_MIDDLE,
        TajweedPattern.qalqalahAtEndPattern.toRegex() to Tajweed.TajweedType.QALQALAH_AT_END,
        TajweedPattern.qalqalahAtStopPattern.toRegex() to Tajweed.TajweedType.QALQALAH_AT_STOP,
        TajweedPattern.idghaamWithGhunnahPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_WITH_GHUNNAH,
        TajweedPattern.idghaamWithoutGhunnahPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_WITHOUT_GHUNNAH,
//        TajweedPattern.idghaamMutamaathilaynPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_MUTAMAATHILAYN,
        TajweedPattern.idghaamMutajaanisaynPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_MUTAJAANISAYN,
        TajweedPattern.idghaamMutaqaaribaynPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_MUTAQAARIBAYN,
//        TajweedPattern.maddFourTimePattern.toRegex() to Tajweed.TajweedType.MADD_FOUR_TIME,
//        TajweedPattern.tafkhiimPattern.toRegex() to Tajweed.TajweedType.TAFKHIIM
    )

    fun getTajweeds(verse: String): List<Tajweed> {
        return patterns.flatMap { (regex, type) ->
            regex.findAll(verse).map { r ->
                val startOffset = when (type) {
                    Tajweed.TajweedType.IQLAAB -> 1
                    Tajweed.TajweedType.QALQALAH_AT_STOP -> 1
                    else -> 0
                }
                val endOffset = when (type) {
                    Tajweed.TajweedType.WAZIB_GHUNNAH -> if (r.range.count() > 2) 1 else 0
                    Tajweed.TajweedType.IKHFAA -> -1
                    Tajweed.TajweedType.IDGHAAM_WITH_GHUNNAH -> -3
                    Tajweed.TajweedType.IDGHAAM_MUTAJAANISAYN -> -2
                    Tajweed.TajweedType.IDGHAAM_MUTAQAARIBAYN -> -2
                    Tajweed.TajweedType.MADD_FOUR_TIME -> if (r.range.count() <= 2) 1 else 0
                    Tajweed.TajweedType.TAFKHIIM -> 2
                    Tajweed.TajweedType.QALQALAH_AT_MIDDLE,
                    Tajweed.TajweedType.QALQALAH_AT_STOP,
                    Tajweed.TajweedType.QALQALAH_AT_END -> 1
                    else -> 0
                }
                Tajweed(type, r.value, r.range.first - startOffset, r.range.last + endOffset)
            }
        }
    }
}