package com.mfoumby.hassan.common.domain

object NumberFormatUtils {
    private val arabicDigits = charArrayOf('٠','١','٢','٣','٤','٥','٦','٧','٨','٩')

    fun toArabic(number: Int): String {
        return number.toString().map {
            if (it.isDigit()) arabicDigits[it - '0'] else it
        }.joinToString("")
    }
}
