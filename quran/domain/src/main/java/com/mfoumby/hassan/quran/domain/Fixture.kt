package com.mfoumby.hassan.quran.domain

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.domain.entity.Constants
import com.mfoumby.hassan.quran.domain.entity.Hizb
import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.QuranSearchResult
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import com.mfoumby.hassan.quran.domain.entity.SurahVersePlayerManifest
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation
import com.mfoumby.hassan.quran.domain.entity.Verse

val surahFixture = Surah(
    number = 1,
    name = "الفاتحة",
    transliteration = "Al-Fatihah",
    type = "Meccan",
    totalVerses = 7,
    translation = "The Opener"
)

val surahFixture2 = Surah(
    number = 2,
    name = "البقرة",
    transliteration = "Al-Baqarah",
    type = "medinan",
    totalVerses = 286,
    translation = "The Cow"
)

val surahFixture3 = Surah(
    number = 113,
    name = "الفلق",
    transliteration = "Al-Falaq",
    type = "meccan",
    totalVerses = 5,
    translation = "The Daybreak"
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

val verseFixture = Verse(
    surahNumber = 1,
    verseNumber = 1,
    text = "بِسۡمِ ٱللَّهِ ٱلرَّحۡمَٰنِ ٱلرَّحِيمِ",
    transliteration = "Bismi Allahi alrrahmani alrraheemi",
    page = 1,
    juzNumber = 1,
    hizbNumber = 1
)

val verseFixture2 = Verse(
    surahNumber = 2,
    verseNumber = 255,
    text = "ٱللَّهُ لَآ إِلَـٰهَ إِلَّا هُوَ ٱلْحَىُّ ٱلْقَيُّومُ ۚ لَا تَأْخُذُهُۥ سِنَةٌۭ وَلَا نَوْمٌۭ ۚ لَّهُۥ مَا فِى ٱلسَّمَـٰوَٰتِ وَمَا فِى ٱلْأَرْضِ ۗ مَن ذَا ٱلَّذِى يَشْفَعُ عِندَهُۥٓ إِلَّا بِإِذْنِهِۦ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَىْءٍۢ مِّنْ عِلْمِهِۦٓ إِلَّا بِمَا شَآءَ ۚ وَسِعَ كُرْسِيُّهُ ٱلسَّمَـٰوَٰتِ وَٱلْأَرْضَ ۖ وَلَا يَـُٔودُهُۥ حِفْظُهُمَا ۚ وَهُوَ ٱلْعَلِىُّ ٱلْعَظِيمُ",
    transliteration = "Allahu la ilaha illa huwa alhayyu alqayyoomu la takhuthuhu sinatun wala nawmun lahu ma fee alssamawati wama fee alardi man tha allathee yashfaAAu AAindahu illa biithnihi yaAAlamu ma bayna aydeehim wama khalfahum wala yuheetoona bishayin min AAilmihi illa bima shaa wasiAAa kursiyyuhu alssamawati waalarda wala yaooduhu hifthuhuma wahuwa alAAaliyyu alAAatheemu",
    page = 42,
    juzNumber = 3,
    hizbNumber = 5
)

val verseFixtures = listOf(
    Verse(
        verseNumber = 1,
        surahNumber = 1,
        text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
        transliteration = "Bismi Allahi alrrahmani alrraheemi",
        page = 1,
        juzNumber = 1,
        hizbNumber = 1
    ),
    Verse(
        verseNumber = 2,
        surahNumber = 1,
        text = "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَـٰلَمِينَ",
        transliteration = "Alhamdu lillahi rabbi alAAalameena",
        page = 1,
        juzNumber = 1,
        hizbNumber = 1
    ),
    Verse(
        verseNumber = 3,
        surahNumber = 1,
        text = "ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
        transliteration = "Alrrahmani alrraheemi",
        page = 1,
        juzNumber = 1,
        hizbNumber = 1
    ),
    Verse(
        verseNumber = 4,
        surahNumber = 1,
        text = "مَـٰلِكِ يَوْمِ ٱلدِّينِ",
        transliteration = "Maliki yawmi alddeeni",
        page = 1,
        juzNumber = 1,
        hizbNumber = 1
    ),
    Verse(
        verseNumber = 5,
        surahNumber = 1,
        text = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
        transliteration = "Iyyaka naAAbudu waiyyaka nastaAAeenu",
        page = 1,
        juzNumber = 1,
        hizbNumber = 1
    ),
    Verse(
        verseNumber = 6,
        surahNumber = 1,
        text = "ٱهْدِنَا ٱلصِّرَٰطَ ٱلْمُسْتَقِيمَ",
        transliteration = "Ihdina alssirata almustaqeema",
        page = 1,
        juzNumber = 1,
        hizbNumber = 1
    ),
    Verse(
        verseNumber = 7,
        surahNumber = 1,
        text = "صِرَٰطَ ٱلَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ ٱلْمَغْضُوبِ عَلَيْهِمْ وَلَا ٱلضَّآلِّينَ",
        transliteration = "Sirata allatheena anAAamta AAalayhim ghayri almaghdoobi AAalayhim wala alddalleena",
        page = 1,
        juzNumber = 1,
        hizbNumber = 1
    )
)

val verseFixtures3 = listOf(
    Verse(
        verseNumber = 1,
        surahNumber = 113,
        text = "قُلۡ أَعُوذُ بِرَبِّ ٱلۡفَلَقِ",
        transliteration = "Qul aAAoothu birabbi alfalaqi",
        page = 604,
        juzNumber = 30,
        hizbNumber = 60
    ),
    Verse(
        verseNumber = 2,
        surahNumber = 113,
        text = "مِن شَرِّ مَا خَلَقَ",
        transliteration = "Min sharri ma khalaqa",
        page = 604,
        juzNumber = 30,
        hizbNumber = 60
    ),
    Verse(
        verseNumber = 3,
        surahNumber = 113,
        text = "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ",
        transliteration = "Wamin sharri ghasiqin itha waqaba",
        page = 604,
        juzNumber = 30,
        hizbNumber = 60
    ),
    Verse(
        verseNumber = 4,
        surahNumber = 113,
        text = "وَمِن شَرِّ ٱلنَّفَّـٰثَٰتِ فِي ٱلۡعُقَدِ",
        transliteration = "Wamin sharri alnnaffathati fee alAAuqadi",
        page = 604,
        juzNumber = 30,
        hizbNumber = 60
    ),
    Verse(
        verseNumber = 5,
        surahNumber = 113,
        text = "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
        transliteration = "Wamin sharri hasidin itha hasada",
        page = 604,
        juzNumber = 30,
        hizbNumber = 60
    )
)

val surahVerseFixture = SurahVerse(
    surah = surahFixture,
    verse = verseFixture
)

val surahVerseFixture2 = SurahVerse(
    surah = surahFixture2,
    verse = verseFixture2
)

val surahVerseFixtures = verseFixtures.map {
    SurahVerse(
        surah = surahFixture,
        verse = it
    )
}

val surahVerseFixtures3 = verseFixtures3.map {
    SurahVerse(
        surah = surahFixture3,
        verse = it
    )
}

val juzFixture = Juz(
    number = surahVerseFixture.verse.juzNumber,
    firstSurahVerse = surahVerseFixture
)

val juzFixtures = surahVerseFixtures.map {
    Juz(
        number = it.verse.juzNumber,
        firstSurahVerse = it
    )
}

val hizbFixture = Hizb(
    number = surahVerseFixture.verse.hizbNumber,
    firstSurahVerse = surahVerseFixture
)

val hizbFixtures = surahVerseFixtures.map {
    Hizb(
        number = it.verse.hizbNumber,
        firstSurahVerse = it
    )
}

val surahVerseTranslationFixture = SurahVerseTranslation(
    verseNumber = 1,
    surahNumber = 1,
    text = "In the Name of Allah—the Most Compassionate, Most Merciful.",
    language = Language.ENGLISH
)

val surahVerseTranslationFixtures = listOf(
    SurahVerseTranslation(
        verseNumber = 1,
        surahNumber = 1,
        text = "In the Name of Allah—the Most Compassionate, Most Merciful.",
        language = Language.ENGLISH
    ),
    SurahVerseTranslation(
        verseNumber = 2,
        surahNumber = 1,
        text = "All praise is for Allah—Lord of all worlds,",
        language = Language.ENGLISH
    ),
    SurahVerseTranslation(
        verseNumber = 3,
        surahNumber = 1,
        text = "the Most Compassionate, Most Merciful,",
        language = Language.ENGLISH
    ),
    SurahVerseTranslation(
        verseNumber = 4,
        surahNumber = 1,
        text = "Master of the Day of Judgment.",
        language = Language.ENGLISH
    ),
    SurahVerseTranslation(
        verseNumber = 5,
        surahNumber = 1,
        text = "You ˹alone˺ we worship and You ˹alone˺ we ask for help.",
        language = Language.ENGLISH
    ),
    SurahVerseTranslation(
        verseNumber = 6,
        surahNumber = 1,
        text = "Guide us along the Straight Path,",
        language = Language.ENGLISH
    ),
    SurahVerseTranslation(
        verseNumber = 7,
        surahNumber = 1,
        text = "the Path of those You have blessed—not those You are displeased with, or those who are astray.",
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
    surah = surahVerseFixture.surah,
    verseNumber = surahVerseFixture.verse.verseNumber,
    audioUri = "https://example.com"
)

val surahVersePlayerManifestFixture = SurahVersePlayerManifest(
    reciter = reciterFixture,
    surahVerseAudios = mapOf((surahVerseFixture.surah.number to surahVerseFixture.verse.verseNumber) to surahVerseAudioFixture),
    state = SurahVersePlayerManifest.State.Playing(surahVerseAudioFixture)
)

val surahVersePreferencesFixture = Constants.DEFAULT_SURAH_VERSE_PREFERENCES.copy(
    displayMode = SurahVersePreferences.DisplayMode.LIST,
    translationLanguage = Language.ENGLISH,
    displayTranslation = true,
    reciter = reciterFixture,
    audioAutomaticScrolling = true,
    surahBookmark = surahVerseFixture2,
    juzBookmark = surahVerseFixture2,
    hizbBookmark = surahVerseFixture2
)

val quranSearchResultFixtures = listOf(
    QuranSearchResult.SurahResult(surahs = listOf(surahFixture, surahFixture2))
)