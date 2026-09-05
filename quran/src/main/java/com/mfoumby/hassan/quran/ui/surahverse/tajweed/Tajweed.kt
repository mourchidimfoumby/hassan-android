package com.mfoumby.hassan.quran.ui.surahverse.tajweed

data class Tajweed(
    val type: TajweedType,
    val word: String,
    val startIndex: Int,
    val endIndex: Int
) {
    companion object {
        private const val GHUNNAH_COLOR = 0xFF22A06B
        private const val IKHFAA_COLOR = GHUNNAH_COLOR
        private const val IDGHAAM_WITH_GHUNNAH_COLOR = GHUNNAH_COLOR
        private const val IDGHAAM_WITHOUT_GHUNNAH_COLOR = 0xFF9E9E9E
    }

    enum class TajweedType(val color: Long) {
        IKHFAA(IKHFAA_COLOR),
        IDGHAAM_WITH_GHUNNAH_NUN_SAKINAH(IDGHAAM_WITH_GHUNNAH_COLOR),
        IDGHAAM_WITH_GHUNNAH_TANWEEN(IDGHAAM_WITH_GHUNNAH_COLOR),
        IDGHAAM_WITHOUT_GHUNNAH_NUN_SAKINAH(IDGHAAM_WITHOUT_GHUNNAH_COLOR),
        IDGHAAM_WITHOUT_GHUNNAH_TANWEEN(IDGHAAM_WITHOUT_GHUNNAH_COLOR),
    }
}