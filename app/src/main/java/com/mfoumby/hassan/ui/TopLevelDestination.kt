package com.mfoumby.hassan.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mfoumby.hassan.common.Route
import com.mfoumby.hassan.quran.ui.QuranRoute

sealed class TopLevelDestination(
    open val badges: Int,
    open val hasNews: Boolean
) {
    abstract val route: Route
    abstract val label: Int
    abstract val filledIcon: Int
    abstract val outlinedIcon: Int
    abstract val iconDescription: Int

    data object Quran: TopLevelDestination(0, false) {
        override val route = QuranRoute
        @StringRes override val label: Int = com.mfoumby.hassan.quran.R.string.quran
        @DrawableRes override val filledIcon: Int = com.mfoumby.hassan.common.R.drawable.ic_fill_book
        @DrawableRes override val outlinedIcon: Int = com.mfoumby.hassan.common.R.drawable.ic_outline_book
        @StringRes override val iconDescription: Int = com.mfoumby.hassan.quran.R.string.quran_destination_icon_description
    }
}

enum class TopLevelDestinationRoute {
    QURAN
}