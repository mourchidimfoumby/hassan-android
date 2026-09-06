package com.mfoumby.hassan.quran.ui.surahverse.tajweed

data class Tajweed(
    val type: TajweedType,
    val word: String,
    val startIndex: Int,
    val endIndex: Int
) {
    companion object {
        private const val GHUNNAH_COLOR = 0xFF43A047
        private const val IKHFAA_COLOR = GHUNNAH_COLOR
        private const val IQLAAB_COLOR = GHUNNAH_COLOR
        private const val IDGHAAM_WITH_GHUNNAH_COLOR = GHUNNAH_COLOR
        private const val IDGHAAM_WITHOUT_GHUNNAH_COLOR = 0xFF9E9E9E
        private const val QALQALAH_COLOR = 0xFF1E88E5
        private const val MADD_FOUR_TIME_COLOR = 0xFFE93323
        private const val MADD_SIX_TIME_COLOR = 0xFF952017
    }

    enum class TajweedType(val color: Long) {
        IKHFAA(IKHFAA_COLOR),
        IQLAAB(IQLAAB_COLOR),
        IDGHAAM_WITH_GHUNNAH_NUN_SAKINAH(IDGHAAM_WITH_GHUNNAH_COLOR),
        IDGHAAM_WITH_GHUNNAH_TANWEEN(IDGHAAM_WITH_GHUNNAH_COLOR),
        IDGHAAM_WITHOUT_GHUNNAH_NUN_SAKINAH(IDGHAAM_WITHOUT_GHUNNAH_COLOR),
        IDGHAAM_WITHOUT_GHUNNAH_TANWEEN(IDGHAAM_WITHOUT_GHUNNAH_COLOR),
        IDGHAAM_SHAFAWI(IDGHAAM_WITH_GHUNNAH_COLOR),
        QALQALAH(QALQALAH_COLOR),
        MADD_FOUR_TIME(MADD_FOUR_TIME_COLOR),
        MADD_SIX_TIME(MADD_SIX_TIME_COLOR),
        MADD_HARF_MUQATTAT(MADD_SIX_TIME_COLOR),
        MADD_SUPERSCRIPT_ALIF(MADD_FOUR_TIME_COLOR),
    }
}