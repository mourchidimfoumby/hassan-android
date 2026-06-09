package com.mfoumby.hassan.quran.domain

import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse

val surahFixture = Surah(
    number = 1,
    name = "الفاتحة",
    transliteration = "Al-Fatihah",
    type = "Meccan",
    totalVerses = 7,
    translation = "The Opener"
)

val surahFixtures = listOf(
    surahFixture,
    Surah(
        number = 2,
        name = "البقرة",
        transliteration = "Al-Baqarah",
        type = "Meccan",
        totalVerses = 286,
        translation = "The Cow"
    ),
    Surah(
        number = 3,
        name = "آل عمران",
        transliteration = "Ali 'Imran",
        type = "Medinan",
        totalVerses = 200,
        translation = "Family of Imran"
    ),
    Surah(
        number = 4,
        name = "النساء",
        transliteration = "Al-Nisa",
        type = "Medinan",
        totalVerses = 176,
        translation = "The Women"
    ),
    Surah(
        number = 5,
        name = "المائدة",
        transliteration = "Al-Ma'idah",
        type = "Medinan",
        totalVerses = 120,
        translation = "The Table Spread"
    )
)

val surahVerseFixture = SurahVerse(
    surahNumber = 2,
    number = 255,
    text = "ٱللَّهُ لَآ إِلَـٰهَ إِلَّا هُوَ ٱلْحَىُّ ٱلْقَيُّومُ ۚ لَا تَأْخُذُهُۥ سِنَةٌۭ وَلَا نَوْمٌۭ ۚ لَّهُۥ مَا فِى ٱلسَّمَـٰوَٰتِ وَمَا فِى ٱلْأَرْضِ ۗ مَن ذَا ٱلَّذِى يَشْفَعُ عِندَهُۥٓ إِلَّا بِإِذْنِهِۦ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَىْءٍۢ مِّنْ عِلْمِهِۦٓ إِلَّا بِمَا شَآءَ ۚ وَسِعَ كُرْسِيُّهُ ٱلسَّمَـٰوَٰتِ وَٱلْأَرْضَ ۖ وَلَا يَـُٔودُهُۥ حِفْظُهُمَا ۚ وَهُوَ ٱلْعَلِىُّ ٱلْعَظِيمُ",
    translation = "Allah! There is no god ˹worthy of worship˺ except Him, the Ever-Living, All-Sustaining. " +
            "Neither drowsiness nor sleep overtakes Him. To Him belongs whatever is in the heavens and whatever is on the earth. " +
            "Who could possibly intercede with Him without His permission? He ˹fully˺ knows what is ahead of them and what is behind them, " +
            "but no one can grasp any of His knowledge—except what He wills ˹to reveal˺. " +
            "His Seat1 encompasses the heavens and the earth, and the preservation of both does not tire Him. For He is the Most High, the Greatest."
)

val surahVerseFixtures = listOf(
    SurahVerse(
        number = 1,
        surahNumber = 1,
        text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
        translation = "In the Name of Allah—the Most Compassionate, Most Merciful."
    ),
    SurahVerse(
        number = 2,
        surahNumber = 1,
        text = "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَـٰلَمِينَ",
        translation = "All praise is for Allah—Lord of all worlds,"
    ),
    SurahVerse(
        number = 3,
        surahNumber = 1,
        text = "ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
        translation = "the Most Compassionate, Most Merciful,"
    ),
    SurahVerse(
        number = 4,
        surahNumber = 1,
        text = "مَـٰلِكِ يَوْمِ ٱلدِّينِ",
        translation = "Master of the Day of Judgment."
    ),
    SurahVerse(
        number = 5,
        surahNumber = 1,
        text = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
        translation = "You ˹alone˺ we worship and You ˹alone˺ we ask for help."
    ),
    SurahVerse(
        number = 6,
        surahNumber = 1,
        text = "ٱهْدِنَا ٱلصِّرَٰطَ ٱلْمُسْتَقِيمَ",
        translation = "Guide us along the Straight Path,"
    ),
    SurahVerse(
        number = 7,
        surahNumber = 1,
        text = "صِرَٰطَ ٱلَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ ٱلْمَغْضُوبِ عَلَيْهِمْ وَلَا ٱلضَّآلِّينَ",
        translation = "the Path of those You have blessed—not those You are displeased with, or those who are astray."
    )
)