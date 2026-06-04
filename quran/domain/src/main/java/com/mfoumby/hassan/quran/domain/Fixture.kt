package com.mfoumby.hassan.quran.domain

val surahFixture = Surah(
    number = 1,
    name = "Al-Fatihah",
    transliteration = "الفاتحة",
    type = "Meccan",
    totalVerses = 7,
    translation = "The Opener"
)

val surahFixtures = listOf(
    surahFixture,
    Surah(
        number = 2,
        name = "Al-Baqarah",
        transliteration = "البقرة",
        type = "Meccan",
        totalVerses = 286,
        translation = "The Cow"
    ),
    Surah(
        number = 3,
        name = "Ali 'Imran",
        transliteration = "آل عمران",
        type = "Medinan",
        totalVerses = 200,
        translation = "Family of Imran"
    ),
    Surah(
        number = 4,
        name = "Al-Nisa",
        transliteration = "النساء",
        type = "Medinan",
        totalVerses = 176,
        translation = "The Women"
    ),
    Surah(
        number = 5,
        name = "Al-Ma'idah",
        transliteration = "المائدة",
        type = "Medinan",
        totalVerses = 120,
        translation = "The Table Spread"
    )
)