package com.mfoumby.hassan.quran.ui.surahverse.tajweed

object TajweedPattern {
    private const val ALIF = 'ا'
    private const val LAM = 'ل'
    private const val NUN = 'ن'
    private const val MEEM = 'م'
    private const val BAA = 'ب'
    private const val YAA = 'ى'
    private const val SHADDAH = '\u0651' // ّ
    private const val SUPERSCRIPT_ALIF = '\u0670' // ٰ
    private const val SUBSCRIPT_ALIF = '\u0656' // ٖ
    private const val SUPERSCRIPT_MEEM = '\u06E2' // ۢ
    private const val INVERTED_DAMMA = '\u0657' // ٗ
    private const val MADDAH = '\u0653' // ٓ
    private const val SPACE = "\\s"
    private val harakaat = listOf(
        '\u064E', // َ
        '\u064F',  // ُ
        '\u0650' // ِ
    )
    private val tanween = listOf(
        '\u064B', // ً
        '\u065E',  // ٞ
        '\u064C', // ٌ
        '\u0657', // ٗ
        '\u064D', // ٍ
    )
    private val sakin = listOf(
        '\u06E1', // ۡ
        '\u0652'  // ْ
    )
    private val stops = listOf(
        "مـ",
        "قلى",
        "\u06DA", // ۚ
        "\u06DC", // ۜ
        "\u06D9", // ۙ
        "\u066A", // ٪
        "\u0615"  // ؕ
    )
    private val maddCharIdghaamTanween = listOf(YAA, ALIF)
    private val ignoreCharBetweenIdghaam = listOf(
        "\u06D6", // ۖ
        "\u06E2", // ۢ
        SPACE
    )
    private val ignoreCharWithTanween = listOf(
        '\u06E2', // ۢ
        '\u06ED'  // ۭ
    )
    private val allHuruf = listOf(
        'ا', 'ب', 'ت', 'ث', 'ج', 'ح', 'خ',
        'د', 'ذ', 'ر', 'ز', 'س', 'ش', 'ص', 'ض',
        'ط', 'ظ', 'ع', 'غ', 'ف', 'ق', 'ك', 'ل',
        'م', 'ن', 'ه', 'و', 'ي', 'ة'
    )
    private val hurufIkhfaa = listOf(
        'ت', 'ث', 'ج', 'د', 'ذ', 'ز',
        'س', 'ش', 'ص', 'ض', 'ط', 'ظ',
        'ف', 'ق', 'ك', 'ک'
    )
    private val hurufIdghaamWithGhunnah = listOf('ي', 'و', 'م', 'ن')
    private val hurufIdghaamWithoutGhunnah = listOf('ر', 'ل')
    private val hurufQalqalah = listOf('ق', 'ط', 'ب', 'ج', 'د')
    private val hurufMuqattaatMaddah = listOf(
        'ن', 'ق', 'ص', 'ع', 'س', 'ل', 'ك', 'م'
    )
    private val hurufMadd = listOf(
        'ا', 'و', 'ي', 'ۦ', 'ۥ'
    )

    private val nuunSakin = buildString {
        append(NUN)
        append('(')
        append(SPACE)
        append('|')
        append('[')
        for (c in allHuruf) append(c)
        append(']')
        append(')')
    }

    private val meemSakin = buildString {
        append(MEEM)
        append('(')
        append(SPACE)
        append('|')
        append('[')
        for (c in allHuruf) append(c)
        append(']')
        append(')')
    }

    private val harakatPattern = buildString {
        append('[')
        for (c in harakaat) append(c)
        for (c in tanween) append(c)
        append(SUPERSCRIPT_ALIF)
        append(SUBSCRIPT_ALIF)
        append(INVERTED_DAMMA)
        append("]?")
    }

    private val commonIdghaamWithGhunnahPattern = buildString {
        append("([")
        for (c in ignoreCharBetweenIdghaam) append(c)
        append("])*")

        append('[')
        for (c in hurufIdghaamWithGhunnah) append(c)
        append(']')

        append(SHADDAH)
        append('?')

        append('[')
        for (c in harakaat) append(c)
        for (c in tanween) append(c)
        append(SUPERSCRIPT_ALIF)
        append(SUBSCRIPT_ALIF)
        append(INVERTED_DAMMA)
        append(']')
    }

    private val commonIdghaamWithoutGhunnahPattern = buildString {
        append("([")
        for (c in ignoreCharBetweenIdghaam) append(c)
        append("])*")

        append('[')
        for (c in hurufIdghaamWithoutGhunnah) append(c)
        append(']')

        append(SHADDAH)
        append('?')
        append(harakatPattern)
        append(ALIF)
        append('?')
    }
    private val qalqalahAtMiddlePattern = buildString {
        append("[")
        for (c in hurufQalqalah) append(c)
        append(']')
        append('[')
        for (c in sakin) append(c)
        append(SPACE)
        append("]")
    }

    private val qalqalahAtEndPattern = buildString {
        append("[")
        for (c in hurufQalqalah) append(c)
        append(']')

        append(SHADDAH)
        append('?')

        append("[")
        for (c in harakaat) append(c)
        for (c in tanween) append(c)
        append("].?$")
    }

    private val qalqalahAtStopPattern = buildString {
        append("[")
        for (c in hurufQalqalah) append(c)
        append(']')

        append(SHADDAH)
        append('?')

        append('[')
        for (c in harakaat) append(c)
        for (c in tanween) append(c)
        append("]")

        append('\u2009')
        append('?')

        append('(')
        append(stops.joinToString("|"))
        append(')')
    }

    val ikhfaaPattern = buildString {
        append('(')
        append(NUN)
        append('[')
        for (c in hurufIkhfaa) append(c)
        append(']')
        append('|')
        append(MEEM)
        append(SPACE)
        append(BAA)
        append(')')

        append(harakatPattern)
    }

    val iqlaabPattern = buildString {
        append('(')
        append(NUN)

        append('|')

        append('[')
        for (c in allHuruf) append(c)
        append(']')
        append(SHADDAH)
        append('?')

        append('(')
        append('[')
        for (c in tanween) append(c)
        append(']')
        append('|')
        append('[')
        for (c in harakaat) append(c)
        append(']')
        append(SUPERSCRIPT_MEEM)
        append(')')
        append('[')
        for (c in ignoreCharWithTanween) append(c)
        append(']')
        append('?')
        append(')')

        append(SPACE)
        append('?')

        append(BAA)
        append(harakatPattern)
    }

    val idghaamWithGhunnahNunSakinahPattern = buildString {
        append(nuunSakin)
        append(commonIdghaamWithGhunnahPattern)
    }

    val idghaamWithGhunnahTanweenPattern = buildString {
        append('[')
        for (c in allHuruf) append(c)
        append(']')
        append('[')
        for (c in tanween) append(c)
        append(']')

        append('[')
        for (c in ignoreCharWithTanween) append(c)
        append(']')
        append('?')

        append('[')
        for (c in maddCharIdghaamTanween) append(c)
        append(']')
        append('?')

        append(commonIdghaamWithGhunnahPattern)
    }

    val idghaamWithoutGhunnahNunSakinahPattern = buildString {
        append(nuunSakin)
        append(commonIdghaamWithoutGhunnahPattern)
    }

    val idghaamWithoutGhunnahTanweenPattern = buildString {
        append('[')
        for (c in allHuruf) append(c)
        append(']')
        append('[')
        for (c in tanween) append(c)
        append(']')

        append('[')
        for (c in ignoreCharWithTanween) append(c)
        append(']')
        append('?')

        append('[')
        for (c in maddCharIdghaamTanween) append(c)
        append(']')
        append('?')

        append(commonIdghaamWithoutGhunnahPattern)
    }

    val idghaamShafawiPattern = buildString {
        append(meemSakin)
        append(MEEM)
        append(SHADDAH)
        append(harakatPattern)
    }

    val qalqalahPattern = buildString {
        append(qalqalahAtMiddlePattern)
        append('|')
        append(qalqalahAtEndPattern)
        append('|')
        append(qalqalahAtStopPattern)
    }

    val maddFourTimePattern = buildString {
        append('[')
        hurufMadd.forEach { append(it) }
        append(']')
        append(MADDAH)
        append('|')
        append(LAM)
        append(SHADDAH)
        append('?')
        append('[')
        for (c in harakaat) append(c)
        append(']')
        append(ALIF)
        append(MADDAH)
    }

    val maddSixTimePattern = buildString {
        append('[')
        hurufMadd.forEach { append(it) }
        append(']')
        append(MADDAH)
        append('[')
        for (c in allHuruf) append(c)
        append(']')
        append(SHADDAH)
    }

    val maddHarfMuqattatPattern = buildString {
        append('[')
        for (c in hurufMuqattaatMaddah) append(c)
        append(']')
        append(MADDAH)
    }

    val maddSuperscriptAlif = buildString {
        append(SUPERSCRIPT_ALIF)
        append(MADDAH)
    }
}