package com.mfoumby.hassan.quran.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mfoumby.hassan.common.Route
import kotlinx.serialization.Serializable

@Serializable
data object QuranBaseRoute: Route
@Serializable
data object QuranRoute: Route

fun NavController.navigateToQuran(navOptions: NavOptions? = null) {
    navigate(route = QuranBaseRoute, navOptions = navOptions)
}

fun NavGraphBuilder.quranSection(
    bottomBar: @Composable () -> Unit,
    onSurahClick: (Int) -> Unit,
    quranDestinations: NavGraphBuilder.() -> Unit
) {
    navigation<QuranBaseRoute>(startDestination = QuranRoute) {
        composable<QuranRoute> {
            QuranDestination(
                bottomBar = bottomBar,
                onSurahClick = onSurahClick
            )
        }
        quranDestinations()
    }
}