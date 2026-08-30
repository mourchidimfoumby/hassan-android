package com.mfoumby.hassan.quran.ui.surahverse.tajweed

data class Tajweed(
    val type: TajweedType,
    val word: String,
    val startIndex: Int,
    val endIndex: Int
) {
    companion object {
        private const val GHUNNAH_COLOR = 0xFF4EAD5B
        private const val IKHFAA_COLOR = 0xFF4EAD5B
        private const val IQLAAB_COLOR = 0xFF4EAD5B
        private const val QALQALAH_COLOR = 0xFF3CA3E8
        private const val IDGHAAM_COLOR = 0xFFB4B4B4
        private const val MADD_FOUR_TIME_COLOR = 0xFFFF0000
        private const val MADD_SIX_TIME_COLOR = 0xFF952017
        private const val TAFKHIIM_COLOR = 0xFF044887
    }

    enum class TajweedType(val color: Long) {
        WAZIB_GHUNNAH(GHUNNAH_COLOR),
        IKHFAA(IKHFAA_COLOR),
        IQLAAB(IQLAAB_COLOR),
        QALQALAH_AT_MIDDLE(QALQALAH_COLOR),
        QALQALAH_AT_END(QALQALAH_COLOR),
        QALQALAH_AT_STOP(QALQALAH_COLOR),
        IDGHAAM_WITH_GHUNNAH(IDGHAAM_COLOR),
        IDGHAAM_WITHOUT_GHUNNAH(IDGHAAM_COLOR),
//        IDGHAAM_MUTAMAATHILAYN(IDGHAAM_COLOR),
        IDGHAAM_MUTAJAANISAYN(IDGHAAM_COLOR),
        IDGHAAM_MUTAQAARIBAYN(IDGHAAM_COLOR),
        MADD_FOUR_TIME(MADD_FOUR_TIME_COLOR),
        MADD_SIX_TIME(MADD_SIX_TIME_COLOR),
        TAFKHIIM(TAFKHIIM_COLOR)
    }
}