package com.mfoumby.hassan.ui

import androidx.lifecycle.ViewModel
import com.mfoumby.hassan.common.Route
import com.mfoumby.hassan.quran.ui.QuranBaseRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NavigationHostViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(NavigationState())
    val uiState: StateFlow<NavigationState> = _uiState

    data class NavigationState(
        val topLevelDestinations: List<TopLevelDestination> = listOf(
            TopLevelDestination.Quran
        ),
        val startDestination: Route = QuranBaseRoute
    )
}