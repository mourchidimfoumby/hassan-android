package com.mfoumby.hassan.quran.ui.surahverse.reciters

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mfoumby.hassan.common.Route
import kotlinx.serialization.Serializable

@Serializable
data object RecitersRoute: Route

fun NavController.navigateToReciters() {
    navigate(route = RecitersRoute)
}

fun NavGraphBuilder.recitersScreen(
    onBackClick: () -> Unit
) {
    composable<RecitersRoute> {
        RecitersDestination(onBackClick = onBackClick)
    }
}