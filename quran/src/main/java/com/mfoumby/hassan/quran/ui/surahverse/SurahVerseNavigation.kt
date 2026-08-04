package com.mfoumby.hassan.quran.ui.surahverse

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mfoumby.hassan.common.Route
import com.mfoumby.hassan.quran.QuranMode
import com.mfoumby.hassan.quran.domain.SurahNumber
import com.mfoumby.hassan.quran.domain.VerseNumber
import kotlinx.serialization.Serializable

@Serializable
data class SurahVerseRoute(val surahNumber: SurahNumber, val verseNumber: VerseNumber?): Route

fun NavController.navigateToSurahVerse(surahNumber: SurahNumber, verseNumber: VerseNumber? = null) {
    navigate(route = SurahVerseRoute(surahNumber, verseNumber))
}

fun NavGraphBuilder.surahVerseScreen(
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onReciterClick: () -> Unit
) {
    composable<SurahVerseRoute> {
        val route = it.toRoute<SurahVerseRoute>()
        SurahVerseDestination(
            quranMode = QuranMode.SurahMode(route.surahNumber, route.verseNumber),
            onBackClick = onBackClick,
            onTranslationLanguageClick = onTranslationLanguageClick,
            onReciterClick = onReciterClick
        )
    }
}