package com.mfoumby.hassan.quran.ui.surahverse.tajweed

object TajweedPattern {
    private val alif = 'ا'
    private val nuun = 'ن'
    private val space = "\\s"
    private val harakaat = listOf('َ', 'ِ', 'ُ')
    private val tanween = listOf('ً', 'ٍ', 'ٌ', 'ٞ')
    private val shaddah = 'ّ'
    private val superscriptAlif = 'ٰ'
    private val subscriptAlif = 'ٖ'
    private val invertedDamma = 'ٗ'
    private val sakin = listOf('ۡ', 'ْ')
    private val hurufIzhaar = listOf(
        'ء', 'ه', 'ع', 'ح', 'غ', 'خ'
    )
    private val hurufIkhfaa = listOf(
        'ت', 'ث', 'ج', 'د', 'ذ', 'ز',
        'س', 'ش', 'ص', 'ض', 'ط', 'ظ',
        'ف', 'ق', 'ك', 'ک'
    )
    private val ignoreCharBetweenIdghaam = listOf("\u06d6", "\u06E2", space)
    private val hurufIdghaamWithGhunnah = listOf('ي', 'و', 'م', 'ن')
    private val hurufIdghaamWithoutGhunnah = listOf('ر', 'ل')
    private val allHuruf = listOf(
        'ا', 'ب', 'ت', 'ث', 'ج', 'ح', 'خ',
        'د', 'ذ', 'ر', 'ز', 'س', 'ش', 'ص', 'ض',
        'ط', 'ظ', 'ع', 'غ', 'ف', 'ق', 'ك', 'ل',
        'م', 'ن', 'ه', 'و', 'ي'
    )
    private val stops = listOf(
        "مـ", "قلى", '\u06da', '\u06dc',
        '\u06d9', '\u066a', '\u0615'
    )
    private val hurufQalqalah = listOf('ق', 'ط', 'ب', 'ج', 'د')

    private val nuunSakin = buildString {
        append('(')
        append(nuun)
        append('[')
        for (c in sakin) append(c)
        append(']')
        append(space)
        append("[^")
        for (c in hurufIzhaar) append(c)
        append(']')
        append('|')

        append(nuun)
        append("\\s")
        append('|')

        append(nuun)
        append('[')
        for (c in allHuruf) append(c)
        append(']')
        append(')')
    }

    private val harakatPattern = buildString {
        append('[')
        for (c in harakaat) append(c)
        for (c in tanween) append(c)
        append(superscriptAlif)
        append(subscriptAlif)
        append(invertedDamma)
        append("]?")
    }

    private val commonIdghaamWithGhunnahPattern = buildString {
        append("([")
        for (c in ignoreCharBetweenIdghaam) append(c)
        append("])*")

        append('[')
        for (c in hurufIdghaamWithGhunnah) append(c)
        append(']')

        append(shaddah)
        append('?')

        append('[')
        for (c in harakaat) append(c)
        for (c in tanween) append(c)
        append(superscriptAlif)
        append(subscriptAlif)
        append(invertedDamma)
        append(']')
    }

    private val commonIdghaamWithoutGhunnahPattern = buildString {
        append("([")
        for (c in ignoreCharBetweenIdghaam) append(c)
        append("])*")

        append('[')
        for (c in hurufIdghaamWithoutGhunnah) append(c)
        append(']')

        append(shaddah)
        append('?')
        append(harakatPattern)
        append(alif)
        append('?')
    }
    private val qalqalahAtMiddlePattern = buildString {
        append("[")
        for (c in hurufQalqalah ) append(c)
        append(']')
        append('[')
        for (c in sakin) append(c)
        append(space)
        append("]")
    }

    private val qalqalahAtEndPattern = buildString {
        append("[")
        for (c in hurufQalqalah) append(c)
        append(']')

        append(shaddah)
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

        append(shaddah)
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
        append(nuun)
        append('[')
        for (c in hurufIkhfaa) append(c)
        append(']')
        append(harakatPattern)
    }

    val idghaamWithGhunnahNunSakinahPattern = buildString {
        append(nuunSakin)
    } + commonIdghaamWithGhunnahPattern

    val idghaamWithGhunnahTanweenPattern = buildString {
        append('[')
        for (c in tanween) append(c)
        append(']')
    } + commonIdghaamWithGhunnahPattern

    val idghaamWithoutGhunnahNunSakinahPattern = buildString {
        append(nuunSakin)
    } + commonIdghaamWithoutGhunnahPattern

    val idghaamWithoutGhunnahTanweenPattern = buildString {
        append('[')
        for (c in tanween) append(c)
        append(']')
        append('[')
        append('ى')
        append(alif)
        append(']')
        append('?')
    } + commonIdghaamWithoutGhunnahPattern

    val qalqalahPattern = buildString {
        append(qalqalahAtMiddlePattern)
        append('|')
        append(qalqalahAtEndPattern)
        append('|')
        append(qalqalahAtStopPattern)
    }
}