package com.mfoumby.hassan.quran.ui.surahverse

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mfoumby.hassan.common.Route
import com.mfoumby.hassan.quran.QuranMode
import kotlinx.serialization.Serializable

@Serializable
data class JuzRoute(val juzNumber: Int): Route

fun NavController.navigateToJuz(juzNumber: Int) {
    navigate(route = JuzRoute(juzNumber))
}

fun NavGraphBuilder.juzScreen(
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onReciterClick: () -> Unit
) {
    composable<JuzRoute> {
        val juzNumber = it.toRoute<JuzRoute>().juzNumber
        SurahVerseDestination(
            quranMode = QuranMode.JuzMode(juzNumber),
            onBackClick = onBackClick,
            onTranslationLanguageClick = onTranslationLanguageClick,
            onReciterClick = onReciterClick
        )
    }
}