package com.mfoumby.hassan.quran.ui.surahverse

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mfoumby.hassan.common.Route
import com.mfoumby.hassan.quran.HizbNumber
import com.mfoumby.hassan.quran.QuranMode
import com.mfoumby.hassan.quran.SurahNumber
import com.mfoumby.hassan.quran.VerseNumber
import kotlinx.serialization.Serializable

@Serializable
data class HizbRoute(
    val hizbNumber: HizbNumber,
    val surahNumber: SurahNumber,
    val verseNumber: VerseNumber?
): Route

fun NavController.navigateToHizbSurahVerse(
    hizbNumber: HizbNumber,
    surahNumber: SurahNumber,
    verseNumber: VerseNumber? = null
) {
    navigate(route = HizbRoute(hizbNumber, surahNumber, verseNumber))
}

fun NavGraphBuilder.hizbSurahVerseScreen(
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onReciterClick: () -> Unit
) {
    composable<HizbRoute> {
        val route = it.toRoute<HizbRoute>()
        SurahVerseDestination(
            quranMode = QuranMode.HizbMode(route.hizbNumber, route.surahNumber, route.verseNumber),
            onBackClick = onBackClick,
            onTranslationLanguageClick = onTranslationLanguageClick,
            onReciterClick = onReciterClick
        )
    }
}