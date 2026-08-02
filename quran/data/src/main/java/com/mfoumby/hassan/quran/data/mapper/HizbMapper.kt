package com.mfoumby.hassan.quran.data.mapper

import com.mfoumby.hassan.quran.data.model.LocalHizb
import com.mfoumby.hassan.quran.data.model.LocalJuz
import com.mfoumby.hassan.quran.domain.entity.Hizb
import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.SurahVerse

fun LocalHizb.toHizb() = Hizb(
    number = number,
    firstSurahVerse = SurahVerse(
        surah = firstSurah.toSurah(),
        verse = firstVerse.toVerse()
    )
)