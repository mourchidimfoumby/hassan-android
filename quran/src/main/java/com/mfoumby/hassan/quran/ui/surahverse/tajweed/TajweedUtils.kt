package com.mfoumby.hassan.quran.ui.surahverse.tajweed

object TajweedUtils {
    private val patterns = listOf(
        TajweedPattern.idghaamWithGhunnahTanweenPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_WITH_GHUNNAH_TANWEEN,
        TajweedPattern.idghaamWithGhunnahNunSakinahPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_WITH_GHUNNAH_NUN_SAKINAH,
        TajweedPattern.idghaamWithoutGhunnahNunSakinahPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_WITHOUT_GHUNNAH_NUN_SAKINAH,
        TajweedPattern.idghaamWithoutGhunnahTanweenPattern.toRegex() to Tajweed.TajweedType.IDGHAAM_WITHOUT_GHUNNAH_TANWEEN,
    )

    fun getTajweed(verse: String): List<Tajweed> {
        return patterns.flatMap { (regex, type) ->
            regex.findAll(verse).map { r ->
                val startOffset = when (type) {
                    Tajweed.TajweedType.IDGHAAM_WITH_GHUNNAH_TANWEEN -> 1
                    Tajweed.TajweedType.IDGHAAM_WITHOUT_GHUNNAH_TANWEEN -> 1
                    else -> 0
                }

                val endOffset = when (type) {
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