package com.mfoumby.hassan.quran.domain

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import com.mfoumby.hassan.quran.domain.entity.SurahVersePlayerData
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation

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
    verseNumber = 255,
    text = "ٱللَّهُ لَآ إِلَـٰهَ إِلَّا هُوَ ٱلْحَىُّ ٱلْقَيُّومُ ۚ لَا تَأْخُذُهُۥ سِنَةٌۭ وَلَا نَوْمٌۭ ۚ لَّهُۥ مَا فِى ٱلسَّمَـٰوَٰتِ وَمَا فِى ٱلْأَرْضِ ۗ مَن ذَا ٱلَّذِى يَشْفَعُ عِندَهُۥٓ إِلَّا بِإِذْنِهِۦ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَىْءٍۢ مِّنْ عِلْمِهِۦٓ إِلَّا بِمَا شَآءَ ۚ وَسِعَ كُرْسِيُّهُ ٱلسَّمَـٰوَٰتِ وَٱلْأَرْضَ ۖ وَلَا يَـُٔودُهُۥ حِفْظُهُمَا ۚ وَهُوَ ٱلْعَلِىُّ ٱلْعَظِيمُ",
    translation = "Allah! There is no god ˹worthy of worship˺ except Him, the Ever-Living, All-Sustaining. " +
            "Neither drowsiness nor sleep overtakes Him. To Him belongs whatever is in the heavens and whatever is on the earth. " +
            "Who could possibly intercede with Him without His permission? He ˹fully˺ knows what is ahead of them and what is behind them, " +
            "but no one can grasp any of His knowledge—except what He wills ˹to reveal˺. " +
            "His Seat1 encompasses the heavens and the earth, and the preservation of both does not tire Him. For He is the Most High, the Greatest."
)

val surahVerseFixtures = listOf(
    SurahVerse(
        verseNumber = 1,
        surahNumber = 1,
        text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
        translation = "In the Name of Allah—the Most Compassionate, Most Merciful."
    ),
    SurahVerse(
        verseNumber = 2,
        surahNumber = 1,
        text = "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَـٰلَمِينَ",
        translation = "All praise is for Allah—Lord of all worlds,"
    ),
    SurahVerse(
        verseNumber = 3,
        surahNumber = 1,
        text = "ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
        translation = "the Most Compassionate, Most Merciful,"
    ),
    SurahVerse(
        verseNumber = 4,
        surahNumber = 1,
        text = "مَـٰلِكِ يَوْمِ ٱلدِّينِ",
        translation = "Master of the Day of Judgment."
    ),
    SurahVerse(
        verseNumber = 5,
        surahNumber = 1,
        text = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
        translation = "You ˹alone˺ we worship and You ˹alone˺ we ask for help."
    ),
    SurahVerse(
        verseNumber = 6,
        surahNumber = 1,
        text = "ٱهْدِنَا ٱلصِّرَٰطَ ٱلْمُسْتَقِيمَ",
        translation = "Guide us along the Straight Path,"
    ),
    SurahVerse(
        verseNumber = 7,
        surahNumber = 1,
        text = "صِرَٰطَ ٱلَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ ٱلْمَغْضُوبِ عَلَيْهِمْ وَلَا ٱلضَّآلِّينَ",
        translation = "the Path of those You have blessed—not those You are displeased with, or those who are astray."
    )
)

val surahVerseTranslationFixture = SurahVerseTranslation(
    number = 1,
    surahNumber = 1,
    translation = "In the Name of Allah—the Most Compassionate, Most Merciful.",
    language = Language.ENGLISH
)

val surahVerseTranslationFixtures = listOf(
    SurahVerseTranslation(
        number = 1,
        surahNumber = 1,
        translation = "In the Name of Allah—the Most Compassionate, Most Merciful.",
        language = Language.ENGLISH
    ),
    SurahVerseTranslation(
        number = 2,
        surahNumber = 1,
        translation = "All praise is for Allah—Lord of all worlds,",
        language = Language.ENGLISH
    ),
    SurahVerseTranslation(
        number = 3,
        surahNumber = 1,
        translation = "the Most Compassionate, Most Merciful,",
        language = Language.ENGLISH
    ),
    SurahVerseTranslation(
        number = 4,
        surahNumber = 1,
        translation = "Master of the Day of Judgment.",
        language = Language.ENGLISH
    ),
    SurahVerseTranslation(
        number = 5,
        surahNumber = 1,
        translation = "You ˹alone˺ we worship and You ˹alone˺ we ask for help.",
        language = Language.ENGLISH
    ),
    SurahVerseTranslation(
        number = 6,
        surahNumber = 1,
        translation = "Guide us along the Straight Path,",
        language = Language.ENGLISH
    ),
    SurahVerseTranslation(
        number = 7,
        surahNumber = 1,
        translation = "the Path of those You have blessed—not those You are displeased with, or those who are astray.",
        language = Language.ENGLISH
    )
)

val reciterFixture = Reciter(
    id = "1",
    name = "Mishary Rashid Al-Afasy",
    imageUrl = "https://cdn.alfaqr.com/images/reciters/mishary-rashid-alafasy-profile.jpeg"
)

val reciterFixtures = listOf(
    reciterFixture,
    Reciter(
        id = "2",
        name = "Abdul Basit Abdul Samad (Murattal)",
        imageUrl = "https://quran-uni.com/wp-content/uploads/abdulbasit-abdulsamad-300x300.jpg"
    ),
    Reciter(
        id = "3",
        name = "Abdul Rahman Al-Sudais",
        imageUrl = "https://www.assabile.com/media/person/200x256/abdul-rahman-al-sudais.png"
    ),
    Reciter(
        id = "4",
        name = "Abu Bakr Al-Shatri",
        imageUrl = "https://cdn.alfaqr.com/images/reciters/abu-bakr-al-shatri-pofile.jpeg"
    ),
    Reciter(
        id = "5",
        name = "Ahmad Al-Ajmi",
        imageUrl = "https://www.assabile.com/media/person/200x256/ahmed-al-ajmi.png"
    ),
    Reciter(
        id = "6",
        name = "Saad Al Ghamdi",
        imageUrl = "https://static.qurancdn.com/images/reciters/16/saad-al-ghamdi-profile.png?v=1"
    ),
    Reciter(
        id = "7",
        name = "Hani Ar-Rifai",
        imageUrl = "https://www.assajda.com/media/person/square/hani-ar-rifai.jpg"
    ),
    Reciter(
        id = "8",
        name = "Ibrahim Al-Akhdar",
        imageUrl = "https://fr.assabile.com/media/person/200x256/ibrahim-al-akdar.png"
    ),
    Reciter(
        id = "9",
        name = "Maher Al-Muaiqly",
        imageUrl = "https://www.assabile.com/media/person/280x219/maher-al-mueaqly.png"
    ),
    Reciter(
        id = "10",
        name = "Muhammad Ayyub",
        imageUrl = "https://upload.wikimedia.org/wikipedia/en/4/40/Muhammad_Ayyub.jpeg"
    )
)

val surahVerseAudioFixture = SurahVerseAudio(
    verseNumber = surahVerseFixture.verseNumber,
    audioUri = "https://example.com"
)

val surahVersePlayerDataFixture = SurahVersePlayerData(
    reciter = reciterFixture,
    surahVerseAudios = listOf(surahVerseAudioFixture),
    state = SurahVersePlayerData.State.Playing(surahVerseAudioFixture)
)

val surahVersePreferencesFixture = SurahVersePreferences(
    displayTranslation = true,
    translationLanguage = Language.ENGLISH,
    reciter = reciterFixture
)