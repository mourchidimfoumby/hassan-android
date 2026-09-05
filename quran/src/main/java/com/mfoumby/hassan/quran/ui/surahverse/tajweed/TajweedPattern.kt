package com.mfoumby.hassan.quran.ui.surahverse.tajweed

object TajweedPattern {
    private val nuun = 'ن'
    private val harakaat = listOf('َ', 'ِ', 'ُ')
    private val tanween = listOf('ً', 'ٍ', 'ٌ', 'ٞ')
    private val shaddah = 'ّ'
    private val superscriptAlif = 'ٰ'
    private val subscriptAlif = 'ٖ'
    private val invertedDamma = 'ٗ'
    private val sakin = listOf('ۡ', 'ْ')
    private val ignoreCharBetweenIdghaam = listOf("\u06d6", "\u06E2", "\\s")
    private val hurufIdghaamWithGhunnah = listOf('ي', 'و', 'م', 'ن')
    private val hurufIdghaamWithoutGhunnah = listOf('ر', 'ل')
//    private val hurufIdghaamMutajaanisayn = listOf(
//        'د' to 'ت',
//        'ت' to 'ط',
//        'ذ' to 'ظ',
//        'ث' to 'ذ',
//        'ب' to 'م'
//    )
//    private val hurufIdghaamMutaqaaribayn = listOf(
//        'ل' to 'ر',
//        'ق' to 'ك'
//    )
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
        append('ا')
        append('?')
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
        append('ا')
        append(']')
        append('?')
    } + commonIdghaamWithoutGhunnahPattern

//    val idghaamMutamaathilaynPattern = buildString {
//        allHuruf.forEachIndexed { index, letter ->
//            append('(')
//            append(letter)
//            append('[')
//            for (c in sakin) append(c)
//            append("\\s")
//            append(']')
//            append(letter)
//            append(shaddah)
//            append('?')
//            append(harakaat)
//            append(')')
//            if (index < allHuruf.lastIndex) append('|')
//        }
//    }
//
//    val idghaamMutajaanisaynPattern = buildString {
//        hurufIdghaamMutajaanisayn.forEachIndexed { index, pair ->
//            append('(')
//            append(pair.first)
//            append('[')
//            for (c in sakin) append(c)
//            append("\\s")
//            append("]?")
//            append(pair.second)
//            append(shaddah)
//            append('?')
//            append(harakaat)
//            append(')')
//            if (index < hurufIdghaamMutajaanisayn.lastIndex) append('|')
//        }
//    }
//
//    val idghaamMutaqaaribaynPattern = buildString {
//        hurufIdghaamMutaqaaribayn.forEachIndexed { index, pair ->
//            append('(')
//            append(pair.first)
//            append('[')
//            for (c in sakin) append(c)
//            append("\\s")
//            append(']')
//            append(pair.second)
//            append(shaddah)
//            append('?')
//            append(harakaat)
//            append(')')
//            if (index < hurufIdghaamMutaqaaribayn.lastIndex) append('|')
//        }
//    }
}