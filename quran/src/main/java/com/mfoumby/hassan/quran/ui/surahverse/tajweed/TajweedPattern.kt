package com.mfoumby.hassan.quran.ui.surahverse.tajweed

object TajweedPattern {
    private const val ALIF = 'ا'
    private const val LAM = 'ل'
    private const val NUN = 'ن'
    private const val MEEM = 'م'
    private const val BAA = 'ب'
    private const val ALIF_KHANJARIYYA = 'ى'
    private const val TATWEEL = '\u0640'  // ـ
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
        '\u0656' // ٖ
    )
    private val sakin = listOf(
        '\u06E1', // ۡ
        '\u0652'  // ْ
    )
    private val stops = listOf(
        "مـ",
        "قلى",
        "\u06D8", // ۘ
        "\u06DA", // ۚ
        "\u06DC", // ۜ
        "\u06D9", // ۙ
        "\u066A", // ٪
        "\u0615"  // ؕ
    )
    private val ignoreChar = listOf(
        "\u06D6", // ۖ
        "\u06E2", // ۢ
        SPACE
    ) + stops
    private val ignoreCharWithTanween = listOf(
        "\u06E2", // ۢ
        "\u06ED",  // ۭ
        ALIF_KHANJARIYYA.toString(),
        ALIF.toString()
    )
    private val allHuruf = listOf(
        'ا', 'ب', 'ت', 'ث', 'ج', 'ح', 'خ',
        'د', 'ذ', 'ر', 'ز', 'س', 'ش', 'ص', 'ض',
        'ط', 'ظ', 'ع', 'غ', 'ف', 'ق', 'ك', 'ل',
        'م', 'ن', 'ه', 'و', 'ي', 'ة', 'ء'
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
        appendCharacterClass(allHuruf)
        append(')')
    }

    private val meemSakin = buildString {
        append(MEEM)
        append('(')
        append(SPACE)
        append('|')
        appendCharacterClass(allHuruf)
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

    private val qalqalahAtMiddlePattern = buildString {
        appendCharacterClass(hurufQalqalah)
        appendCharacterClassStr(sakin.map { it.toString() } + listOf(SPACE))
    }

    private val qalqalahAtEndPattern = buildString {
        appendCharacterClass(hurufQalqalah)
        append("$SHADDAH?")
        appendCharacterClass(harakaat + tanween)
        append(".?$")
    }

    private val qalqalahAtStopPattern = buildString {
        appendCharacterClass(hurufQalqalah)
        append(SHADDAH)
        append('?')
        appendCharacterClass(harakaat + tanween)
        append('\u2009')
        append('?')
        append('(')
        append(stops.joinToString("|"))
        append(')')
    }

    val ghunnahPattern = buildString {
        append('(')
        append(NUN)
        append('|')
        append(MEEM)
        append(')')
        append(SHADDAH)
        appendCharacterClass(harakaat)
    }

    val ikhfaaPattern = buildString {
        append('(')
        append('(')
        append(NUN)
        append('|')
        appendCharacterClass(allHuruf)
        appendCharacterClass(tanween)
        appendCharacterClassStr(ignoreCharWithTanween)
        append("?")
        append(')')

        append("(")
        appendCharacterClassStr(ignoreChar)
        append(")*")
        appendCharacterClass(hurufIkhfaa)

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
        append("$SUPERSCRIPT_MEEM?")
        append('|')
        appendCharacterClass(allHuruf)
        append("$SHADDAH?")
        append('(')
        appendCharacterClass(tanween)
        append('|')
        appendCharacterClass(harakaat)
        append(SUPERSCRIPT_MEEM)
        append(')')
        appendCharacterClassStr(ignoreCharWithTanween)
        append("?")
        append(')')

        append("$SPACE?")
        append(BAA)
        append(harakatPattern)
    }

    val idghaamWithGhunnahPattern = buildString {
        append('(')
        append(nuunSakin)
        append('|')
        appendCharacterClass(allHuruf)
        append("$SHADDAH?")
        appendCharacterClass(tanween)
        appendCharacterClassStr(ignoreCharWithTanween)
        append("?")
        append(')')

        append("(")
        appendCharacterClassStr(ignoreChar)
        append(")*")
        appendCharacterClass(hurufIdghaamWithGhunnah)
        append("$SHADDAH?")
        appendCharacterClass(
            harakaat +
                tanween +
                listOf(
                    SUPERSCRIPT_ALIF,
                    SUBSCRIPT_ALIF,
                    INVERTED_DAMMA
                )
        )
    }

    val idghaamWithoutGhunnahPattern = buildString {
        append('(')
        append(nuunSakin)
        append('|')
        appendCharacterClass(allHuruf)
        appendCharacterClass(tanween)
        appendCharacterClassStr(ignoreCharWithTanween)
        append('?')
        append(')')

        append("(")
        appendCharacterClassStr(ignoreChar)
        append(")*")
        appendCharacterClass(hurufIdghaamWithoutGhunnah)
        append("$SHADDAH?")
        append(harakatPattern)
        append("$ALIF?")
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
        appendCharacterClass(hurufMadd)
        append(MADDAH)
        append('|')
        append(LAM)
        append("$SHADDAH?")
        appendCharacterClass(harakaat)
        append(ALIF)
        append(MADDAH)
    }

    val maddSixTimePattern = buildString {
        appendCharacterClass(hurufMadd)
        append(MADDAH)
        appendCharacterClass(allHuruf)
        append(SHADDAH)
    }

    val maddHarfMuqattatPattern = buildString {
        appendCharacterClass(hurufMuqattaatMaddah)
        append(MADDAH)
    }

    val maddSuperscriptAlif = buildString {
        appendCharacterClass(allHuruf)
        appendCharacterClass(harakaat)
        append("$TATWEEL?")
        appendCharacterClass(hurufMadd + listOf(ALIF_KHANJARIYYA))
        append('?')
        append(SUPERSCRIPT_ALIF)
        append(MADDAH)
    }

    private fun StringBuilder.appendCharacterClassStr(list: List<String>) {
        append('[')
        list.forEach(::append)
        append(']')
    }

    private fun StringBuilder.appendCharacterClass(list: List<Char>) {
        append('[')
        list.forEach(::append)
        append(']')
    }
}