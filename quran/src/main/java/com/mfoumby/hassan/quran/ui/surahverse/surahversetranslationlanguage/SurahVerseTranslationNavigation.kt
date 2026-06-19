package com.mfoumby.hassan.quran.ui.surahverse.surahversetranslationlanguage

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mfoumby.hassan.common.Route
import kotlinx.serialization.Serializable

@Serializable
data object SurahVerseTranslationLanguageRoute: Route

fun NavController.navigateToSurahVerseTranslationLanguage() {
    navigate(route = SurahVerseTranslationLanguageRoute)
}

fun NavGraphBuilder.surahVerseTranslationLanguageScreen(
    onBackClick: () -> Unit
) {
    composable<SurahVerseTranslationLanguageRoute> {
        SurahVerseTranslationLanguageDestination(onBackClick = onBackClick)
    }
}