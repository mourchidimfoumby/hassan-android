package com.mfoumby.hassan.quran.ui.surahverse

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mfoumby.hassan.common.Route
import com.mfoumby.hassan.quran.QuranMode
import kotlinx.serialization.Serializable

@Serializable
data class HizbRoute(val hizbNumber: Int): Route

fun NavController.navigateToHizb(hizbNumber: Int) {
    navigate(route = HizbRoute(hizbNumber))
}

fun NavGraphBuilder.hizbScreen(
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onReciterClick: () -> Unit
) {
    composable<HizbRoute> {
        val hizbNumber = it.toRoute<HizbRoute>().hizbNumber
        SurahVerseDestination(
            quranMode = QuranMode.HizbMode(hizbNumber),
            onBackClick = onBackClick,
            onTranslationLanguageClick = onTranslationLanguageClick,
            onReciterClick = onReciterClick
        )
    }
}