package com.mfoumby.hassan.quran.ui.surahverse.tajweed

object TajweedPattern {
    private val alif = 'ا'
    private val lam = 'ل'
    private val nuun = 'ن'
    private val meem = 'م'
    private val baa = 'ب'
    private val shaddah = 'ّ'
    private val superscriptAlif = 'ٰ'
    private val subscriptAlif = 'ٖ'
    private val invertedDamma = 'ٗ'
    private val maddah = 'ٓ'
    private val space = "\\s"
    private val harakaat = listOf('َ', 'ِ', 'ُ')
    private val tanween = listOf('ً', 'ٍ', 'ٌ', 'ٞ')
    private val sakin = listOf('ۡ', 'ْ')
    private val stops = listOf(
        "مـ", "قلى", '\u06da', '\u06dc',
        '\u06d9', '\u066a', '\u0615'
    )
    private val maddCharIdghaamTanween = listOf('ى', alif)
    private val ignoreCharBetweenIdghaam = listOf("\u06d6", "\u06E2", space)
    private val ignoreCharBetweenIdghaamTanween = listOf("\u06ED")
    private val allHuruf = listOf(
        'ا', 'ب', 'ت', 'ث', 'ج', 'ح', 'خ',
        'د', 'ذ', 'ر', 'ز', 'س', 'ش', 'ص', 'ض',
        'ط', 'ظ', 'ع', 'غ', 'ف', 'ق', 'ك', 'ل',
        'م', 'ن', 'ه', 'و', 'ي'
    )
    private val hurufIzhaar = listOf(
        'ء', 'ه', 'ع', 'ح', 'غ', 'خ'
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
        append('(')
        append(nuun)
        append('(')
        append('[')
        for (c in sakin) append(c)
        append(']')
        append('|')

        append(space)
        append('|')

        append('[')
        for (c in allHuruf) append(c)
        append(']')
        append(')')
        append(')')
    }

    private val meemSakin = buildString {
        append('(')
        append(meem)
        append('(')
        append('[')
        for (c in sakin) append(c)
        append(']')
        append('|')

        append(space)
        append('|')

        append('[')
        for (c in allHuruf) append(c)
        append(']')
        append(')')
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
        append(commonIdghaamWithGhunnahPattern)
    }

    val idghaamWithGhunnahTanweenPattern = buildString {
        append('[')
        for (c in tanween) append(c)
        append(']')

        append('[')
        append(ignoreCharBetweenIdghaamTanween)
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
        for (c in tanween) append(c)
        append(']')

        append('[')
        append(ignoreCharBetweenIdghaamTanween)
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
        append(meem)
        append(shaddah)
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
        append(maddah)
        append('|')
        append(lam)
        append(shaddah)
        append('?')
        append('[')
        for (c in harakaat) append(c)
        append(']')
        append(alif)
        append(maddah)
    }

    val maddSixTimePattern = buildString {
        append('[')
        hurufMadd.forEach { append(it) }
        append(']')
        append(maddah)
        append('[')
        for (c in allHuruf) append(c)
        append(']')
        append(shaddah)
    }

    val maddHarfMuqattatPattern = buildString {
        append('[')
        for (c in hurufMuqattaatMaddah) append(c)
        append(']')
        append(maddah)
    }

    val maddSuperscriptAlif = buildString {
        append(superscriptAlif)
        append(maddah)
    }
}