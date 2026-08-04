package com.mfoumby.hassan.quran.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mfoumby.hassan.common.Route
import com.mfoumby.hassan.quran.domain.HizbNumber
import com.mfoumby.hassan.quran.domain.JuzNumber
import com.mfoumby.hassan.quran.domain.SurahNumber
import com.mfoumby.hassan.quran.domain.VerseNumber
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
    onSurahClick: (SurahNumber) -> Unit,
    onJuzClick: (JuzNumber, SurahNumber) -> Unit,
    onHizbClick: (HizbNumber, SurahNumber) -> Unit,
    onSurahBookmarkClick: (SurahNumber, VerseNumber?) -> Unit,
    onJuzBookmarkClick: (JuzNumber, SurahNumber, VerseNumber?) -> Unit,
    onHizbBookmarkClick: (HizbNumber, SurahNumber, VerseNumber?) -> Unit,
    quranDestinations: NavGraphBuilder.() -> Unit
) {
    navigation<QuranBaseRoute>(startDestination = QuranRoute) {
        composable<QuranRoute> {
            QuranDestination(
                bottomBar = bottomBar,
                onSurahClick = onSurahClick,
                onJuzClick = onJuzClick,
                onHizbClick = onHizbClick,
                onSurahBookmarkClick = onSurahBookmarkClick,
                onJuzBookmarkClick = onJuzBookmarkClick,
                onHizbBookmarkClick = onHizbBookmarkClick
            )
        }
        quranDestinations()
    }
}