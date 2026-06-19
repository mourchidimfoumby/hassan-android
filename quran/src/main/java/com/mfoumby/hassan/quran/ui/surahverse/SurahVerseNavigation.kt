package com.mfoumby.hassan.quran.ui.surahverse

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mfoumby.hassan.common.Route
import kotlinx.serialization.Serializable

@Serializable
data class SurahVerseRoute(val surahNumber: Int): Route

fun NavController.navigateToSurahVerse(surahNumber: Int) {
    navigate(route = SurahVerseRoute(surahNumber))
}

fun NavGraphBuilder.surahVerseScreen(
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit
) {
    composable<SurahVerseRoute> {
        val surahNumber = it.toRoute<SurahVerseRoute>().surahNumber
        SurahVerseDestination(
            surahNumber = surahNumber,
            onBackClick = onBackClick,
            onTranslationLanguageClick = onTranslationLanguageClick
        )
    }
}