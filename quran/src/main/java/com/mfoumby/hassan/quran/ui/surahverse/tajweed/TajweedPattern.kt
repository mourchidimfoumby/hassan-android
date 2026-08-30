package com.mfoumby.hassan.quran.ui.surahverse.tajweed

object TajweedPattern {
    private val ikhfaa = listOf(
        'ت', 'ث', 'ج', 'د', 'ذ', 'ز',
        'س', 'ش', 'ص', 'ض', 'ط', 'ظ',
        'ف', 'ق', 'ك', 'ک'
    )
    private val qalqalah = listOf('ق', 'ط', 'ب', 'ج', 'د')
    private val alif = 'ا'
    private val meem = 'م'
    private val nuun = 'ن'
    private val baa = 'ب'
    private val harakaat = listOf('َ', 'ِ', 'ُ')
    private val tanween = listOf('ً', 'ٍ', 'ٌ', 'ٞ')
    private val shaddah = 'ّ'
    private val maddah = 'ٓ'
    private val smallHighMaddah = 'ۤ'
    private val superscriptAlif = 'ٰ'
    private val subscriptAlif = 'ٖ'
    private val invertedDamma = 'ٗ'
    private val sakin = listOf('ۡ', 'ْ')
    private val meemIsolated = listOf('ۢ', 'ۭ')
    private val stops = listOf(
        "مـ", "قلى", '\u06da', '\u06dc',
        '\u06d9', '\u066a', '\u0615'
    )
    private val ignoreCharBetweenIdghaamGhunnah = listOf("\u06d6", "\u06E2", "\\s")
    private val hurufIdghaamWithGhunnah = listOf('ي', 'ى', 'و', 'م', 'ن')
    private val hurufIdghaamWithoutGhunnah = listOf('ر', 'ل')
    private val hurufIdghaamMutajaanisayn = listOf(
        'د' to 'ت',
        'ت' to 'ط',
        'ذ' to 'ظ',
        'ث' to 'ذ',
        'ب' to 'م'
    )
    private val hurufIdghaamMutaqaaribayn = listOf(
        'ل' to 'ر',
        'ق' to 'ك'
    )
    private val hurufTafkhiim = listOf(
        'خ', 'ص', 'ض', 'غ', 'ط', 'ق', 'ظ'
    )
    private val hurufMadd = listOf(
        'ا', 'و', 'ي'
    )
    private val hurufHamza = listOf(
        'ء', 'أ', 'إ', 'ؤ', 'ئ'
    )
    private val hurufIzhaar = listOf(
        'ء', 'ه', 'ع', 'ح', 'غ', 'خ'
    )
    private val allHuruf = listOf(
        'ا', 'ب', 'ت', 'ث', 'ج', 'ح', 'خ',
        'د', 'ذ', 'ر', 'ز', 'س', 'ش', 'ص', 'ض',
        'ط', 'ظ', 'ع', 'غ', 'ف', 'ق', 'ك', 'ل',
        'م', 'ن', 'ه', 'و', 'ي'
    )

    private val nuunSakin = buildString {
        append('(')
        append(nuun)
        append('[')
        for (c in sakin) append(c)
        append(']')
        append("\\s")
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

    val wazibGhunnahPattern = buildString {
        append(nuunSakin)
        append('|')

        append('[')
        append(nuun)
        append(meem)
        append(']')
        append(shaddah)
        append(harakatPattern)
        append(maddah)
        append('?')
    }

    val ikhfaaPattern = buildString {
        append(nuunSakin)
        append('[')
        for (c in ikhfaa) append(c)
        append(']')
        append(harakatPattern)
    }

    val iqlaabPattern = buildString {
        append(nuunSakin)
        append('[')
        for (c in meemIsolated) append(c)
        append(']')
        append("? ?")
        append(baa)
        append(harakatPattern)
    }

    val idghaamWithGhunnahPattern = buildString {
        append("(?:")
        append(nuunSakin)
        append("|")
        append('[')
        for (c in tanween) append(c)
        append(']')
        append(")")

        append("([")
        for (c in ignoreCharBetweenIdghaamGhunnah) append(c)
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

    val idghaamWithoutGhunnahPattern = buildString {
        append(nuunSakin)
        append('[')
        for (c in hurufIdghaamWithoutGhunnah) append(c)
        append(']')
        append(shaddah)
        append('?')
        append(harakatPattern)
    }

    val idghaamMutamaathilaynPattern = buildString {
        allHuruf.forEachIndexed { index, letter ->
            append('(')
            append(letter)
            append('[')
            for (c in sakin) append(c)
            append("\\s")
            append(']')
            append(letter)
            append(shaddah)
            append('?')
            append(harakaat)
            append(')')
            if (index < allHuruf.lastIndex) append('|')
        }
    }

    val idghaamMutajaanisaynPattern = buildString {
        hurufIdghaamMutajaanisayn.forEachIndexed { index, pair ->
            append('(')
            append(pair.first)
            append('[')
            for (c in sakin) append(c)
            append("\\s")
            append("]?")
            append(pair.second)
            append(shaddah)
            append('?')
            append(harakaat)
            append(')')
            if (index < hurufIdghaamMutajaanisayn.lastIndex) append('|')
        }
    }

    val idghaamMutaqaaribaynPattern = buildString {
        hurufIdghaamMutaqaaribayn.forEachIndexed { index, pair ->
            append('(')
            append(pair.first)
            append('[')
            for (c in sakin) append(c)
            append("\\s")
            append(']')
            append(pair.second)
            append(shaddah)
            append('?')
            append(harakaat)
            append(')')
            if (index < hurufIdghaamMutaqaaribayn.lastIndex) append('|')
        }
    }

    val qalqalahAtMiddlePattern = buildString {
        append("([")
        for (c in qalqalah) append(c)
        append(']')
        append('[')
        for (c in sakin) append(c)
        append("\\s")
        append("])")
    }

    val qalqalahAtEndPattern = buildString {
        append("[")
        for (c in qalqalah) append(c)
        append(']')

        append(shaddah)
        append('?')

        append("([")
        for (c in harakaat) append(c)
        for (c in tanween) append(c)
        append("]).?$")
    }

    val qalqalahAtStopPattern = buildString {
        append("[")
        for (c in qalqalah) append(c)
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

    val maddFourTimePattern = buildString {
        append("(?:")
        append('[')
        hurufMadd.forEach { append(it) }
        append(']')
        append(maddah)
        append('?')
        append("\\s")
        append('?')
        append('[')
        hurufHamza.forEach { append(it) }
        append(']')
        append('|')

        append(superscriptAlif)
        append(maddah)
        append(')')
    }

    val tafkhiimPattern = buildString {
        append('[')
        for (c in hurufTafkhiim) append(c)
        append(']')
    }
}