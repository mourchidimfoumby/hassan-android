package com.mfoumby.hassan.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mfoumby.hassan.common.Route
import com.mfoumby.hassan.quran.ui.QuranBaseRoute
import com.mfoumby.hassan.quran.ui.navigateToQuran
import com.mfoumby.hassan.quran.ui.quranSection
import com.mfoumby.hassan.quran.ui.quransearch.navigateToQuranSearch
import com.mfoumby.hassan.quran.ui.quransearch.quranSearchScreen
import com.mfoumby.hassan.quran.ui.surahverse.hizbSurahVerseScreen
import com.mfoumby.hassan.quran.ui.surahverse.juzSurahVerseScreen
import com.mfoumby.hassan.quran.ui.surahverse.navigateToHizbSurahVerse
import com.mfoumby.hassan.quran.ui.surahverse.navigateToJuzSurahVerse
import com.mfoumby.hassan.quran.ui.surahverse.navigateToSurahVerse
import com.mfoumby.hassan.quran.ui.surahverse.reciters.navigateToReciters
import com.mfoumby.hassan.quran.ui.surahverse.reciters.recitersScreen
import com.mfoumby.hassan.quran.ui.surahverse.surahVerseScreen
import com.mfoumby.hassan.quran.ui.surahverse.surahversetranslationlanguage.navigateToSurahVerseTranslationLanguage
import com.mfoumby.hassan.quran.ui.surahverse.surahversetranslationlanguage.surahVerseTranslationLanguageScreen
import com.mfoumby.hassan.ui.components.MainBottomBar
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data object SplashRoute: Route

@Composable
fun NavigationHost(
    viewModel: NavigationHostViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    val currentEntry = navController.currentBackStackEntryAsState()
    val navOptions = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setRestoreState(true)
        .setPopUpTo(
            route = QuranBaseRoute,
            inclusive = false,
            saveState = true
        )
        .build()

    fun NavController.navigateToTopLevelDestination(destination: TopLevelDestinationRoute) {
        when (destination) {
            TopLevelDestinationRoute.QURAN -> {
                popBackStack()
                navigateToQuran(navOptions = navOptions)
            }
        }
    }

    val bottomBar: @Composable () -> Unit = {
        MainBottomBar(
            onTopLevelDestinationClick = navController::navigateToTopLevelDestination,
            currentRoute = currentEntry.value?.destination,
            topLevelDestinations = uiState.topLevelDestinations
        )
    }

    NavHost(
        navController = navController,
        startDestination = uiState.startDestination
    ) {
        composable<SplashRoute> {}

        quranSection(
            bottomBar = bottomBar,
            onSurahClick = navController::navigateToSurahVerse,
            onJuzClick = navController::navigateToJuzSurahVerse,
            onHizbClick = navController::navigateToHizbSurahVerse,
            onSurahBookmarkClick = navController::navigateToSurahVerse,
            onJuzBookmarkClick = navController::navigateToJuzSurahVerse,
            onHizbBookmarkClick = navController::navigateToHizbSurahVerse,
            onQuranSearchClick = navController::navigateToQuranSearch
        ) {
            surahVerseScreen(
                onBackClick = navController::popBackStack,
                onTranslationLanguageClick = navController::navigateToSurahVerseTranslationLanguage,
                onReciterClick = navController::navigateToReciters
            )

            juzSurahVerseScreen(
                onBackClick = navController::popBackStack,
                onTranslationLanguageClick = navController::navigateToSurahVerseTranslationLanguage,
                onReciterClick = navController::navigateToReciters
            )

            hizbSurahVerseScreen(
                onBackClick = navController::popBackStack,
                onTranslationLanguageClick = navController::navigateToSurahVerseTranslationLanguage,
                onReciterClick = navController::navigateToReciters
            )

            surahVerseTranslationLanguageScreen(onBackClick = navController::popBackStack)

            recitersScreen(onBackClick = navController::popBackStack)

            quranSearchScreen(
                onBackClick = navController::popBackStack,
                onSurahClick = navController::navigateToSurahVerse
            )
        }
    }
}