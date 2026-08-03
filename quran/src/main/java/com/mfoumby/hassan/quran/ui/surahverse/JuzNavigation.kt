package com.mfoumby.hassan.quran.ui.surahverse

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mfoumby.hassan.common.Route
import com.mfoumby.hassan.quran.JuzNumber
import com.mfoumby.hassan.quran.QuranMode
import com.mfoumby.hassan.quran.SurahNumber
import com.mfoumby.hassan.quran.VerseNumber
import kotlinx.serialization.Serializable

@Serializable
data class JuzRoute(
    val juzNumber: JuzNumber,
    val surahNumber: SurahNumber,
    val verseNumber: VerseNumber?
): Route

fun NavController.navigateToJuzSurahVerse(
    juzNumber: JuzNumber,
    surahNumber: SurahNumber,
    verseNumber: VerseNumber? = null
) {
    navigate(route = JuzRoute(juzNumber, surahNumber, verseNumber))
}

fun NavGraphBuilder.juzSurahVerseScreen(
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onReciterClick: () -> Unit
) {
    composable<JuzRoute> {
        val route = it.toRoute<JuzRoute>()
        SurahVerseDestination(
            quranMode = QuranMode.JuzMode(route.juzNumber, route.surahNumber, route.verseNumber),
            onBackClick = onBackClick,
            onTranslationLanguageClick = onTranslationLanguageClick,
            onReciterClick = onReciterClick
        )
    }
}