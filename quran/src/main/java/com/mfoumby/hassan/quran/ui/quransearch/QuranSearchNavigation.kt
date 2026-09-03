package com.mfoumby.hassan.quran.ui.quransearch

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mfoumby.hassan.common.Route
import com.mfoumby.hassan.quran.domain.SurahNumber
import kotlinx.serialization.Serializable

@Serializable
data object QuranSearchRoute: Route

fun NavController.navigateToQuranSearch() {
    navigate(route = QuranSearchRoute)
}

fun NavGraphBuilder.quranSearchScreen(
    onBackClick: () -> Unit,
    onSurahClick: (SurahNumber) -> Unit
) {
    composable<QuranSearchRoute> {
        QuranSearchDestination(
            onBackClick = onBackClick,
            onSurahClick = onSurahClick
        )
    }
}