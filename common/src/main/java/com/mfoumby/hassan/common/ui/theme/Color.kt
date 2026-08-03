package com.mfoumby.hassan.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val white = Color(0xFFFFFFFF)
internal val black = Color(0xFF121212)
internal val primaryLight = Color(0xFF1f4F21)
internal val primaryContainerLight = Color(0xFFDDE7DD)
internal val onPrimaryContainerLight = Color(0xFF454D45)
internal val onPrimaryLight = white
internal val secondaryLight = Color(0xFF1f4F21)
internal val secondaryContainerLight = Color(0xFFE0F8DE)
internal val onSecondaryContainerLight = Color(0xFF192B19)
internal val tertiaryLight = Color(0xFF1f4F21)
internal val backgroundLight = Color(0xFFFFFFFF)
internal val onBackgroundLight = black
internal val errorLight = Color(0xFFED5245)
internal val surfaceLight = backgroundLight
internal val onSurfaceLight = black
internal val onSurfaceVariantLight = Color(0xFF4F4F4F)
internal val inverseSurfaceLight = Color(0xFF303330)
internal val inverseOnSurfaceLight = Color(0xFFEFF4EF)
internal val surfaceContainerLight = Color(0xFFEEF7ED)
internal val surfaceContainerHighLight = Color(0xFFE7F0E6)
internal val surfaceContainerHighestLight = Color(0xFFE1E9E0)
internal val surfaceContainerLowLight = surfaceContainerLight
internal val surfaceVariantLight = Color(0xFFE0ECE1)
internal val outlineLight = Color(0xFF747E74)
internal val outlineVariantLight = Color(0xFFC4D0C4)

internal val primaryDark = Color(0xFFBCFFBD)
internal val primaryContainerDark = Color(0xFF445844)
internal val onPrimaryContainerDark = Color(0xFFE3E3E3)
internal val onPrimaryDark = white
internal val secondaryContainerDark = Color(0xFF455844)
internal val onSecondaryContainerDark = white
internal val backgroundDark = Color(0xFF191919)
internal val onBackgroundDark = white
internal val surfaceDark = backgroundDark
internal val onSurfaceDark = white
internal val onSurfaceVariantDark = Color(0xFFD2D2D2)
internal val inverseSurfaceDark = white
internal val inverseOnSurfaceDark = Color(0xFF303330)
internal val surfaceVariantDark = Color(0xFF464F45)
internal val surfaceContainerDark = Color(0xFF20261F)
internal val surfaceContainerHighDark = Color(0xFF293029)
internal val surfaceContainerHighestDark = Color(0xFF353B34)
internal val surfaceContainerLowDark = surfaceContainerDark
internal val errorDark = Color(0xFFD64A4C)
internal val outlineDark = Color(0xFF939393)
internal val outlineVariantDark = Color(0xFF3C3C3C)

internal val lightGrey = Color(0xFFE0E0E0)

internal val darkGrey = Color(0xFF323232)

val ColorScheme.loadingImageBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF323232) else Color(0xFFEEEEEE)

val ColorScheme.topAppBarColor: TopAppBarColors
    @Composable
    get() = TopAppBarDefaults.topAppBarColors(
        containerColor = surface,
        scrolledContainerColor = surface
    )