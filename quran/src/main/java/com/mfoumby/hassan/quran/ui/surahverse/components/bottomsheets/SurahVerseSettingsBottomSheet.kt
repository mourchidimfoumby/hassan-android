package com.mfoumby.hassan.quran.ui.surahverse.components.bottomsheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.common.resId
import com.mfoumby.hassan.common.roundedFlagResId
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.SectionTitle
import com.mfoumby.hassan.common.ui.components.SimpleAsyncImage
import com.mfoumby.hassan.common.ui.components.SimpleBottomSheet
import com.mfoumby.hassan.common.ui.components.SimpleSwitch
import com.mfoumby.hassan.common.ui.extension.mediumSpacing
import com.mfoumby.hassan.common.ui.extension.smallSpacing
import com.mfoumby.hassan.common.ui.theme.padding
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.ArabicTextFont
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.reciterFixture
import com.mfoumby.hassan.quran.ui.resId
import com.mfoumby.hassan.quran.ui.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahVerseSettingsBottomSheet(
    onDismissRequest: () -> Unit,
    displayMode: SurahVersePreferences.DisplayMode,
    displayTajweed: Boolean,
    arabicTextFont: ArabicTextFont,
    arabicTextFontSize: Int,
    translationLanguage: Language?,
    displayTranslation: Boolean,
    reciter: Reciter?,
    audioAutomaticScrolling: Boolean,
    onDisplayModeClick: (SurahVersePreferences.DisplayMode) -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onArabicTextFontChange: (ArabicTextFont) -> Unit,
    onIncreaseArabicTextFontSizeClick: () -> Unit,
    onDecreaseArabicTextFontSizeClick: () -> Unit,
    onDisplayTajweedChange: (Boolean) -> Unit,
    onDisplayTranslationChange: (Boolean) -> Unit,
    onReciterClick: () -> Unit,
    onAutomaticScrollingChange: (Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    SimpleBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    ) {
        SurahVerseSettingsBottomSheetContent(
            displayMode = displayMode,
            displayTajweed = displayTajweed,
            translationLanguage = translationLanguage,
            displayTranslation = displayTranslation,
            arabicTextFont = arabicTextFont,
            arabicTextFontSize = arabicTextFontSize,
            reciter = reciter,
            audioAutomaticScrolling = audioAutomaticScrolling,
            onDisplayModeClick = onDisplayModeClick,
            onTranslationLanguageClick = onTranslationLanguageClick,
            onDisplayTranslationChange = onDisplayTranslationChange,
            onArabicTextFontChange = onArabicTextFontChange,
            onIncreaseArabicTextFontSizeClick = onIncreaseArabicTextFontSizeClick,
            onDecreaseArabicTextFontSizeClick = onDecreaseArabicTextFontSizeClick,
            onDisplayTajweedChange = onDisplayTajweedChange,
            onReciterClick = onReciterClick,
            onAutomaticScrollingChange = onAutomaticScrollingChange
        )
    }
}

@Composable
private fun SurahVerseSettingsBottomSheetContent(
    displayMode: SurahVersePreferences.DisplayMode,
    displayTajweed: Boolean,
    translationLanguage: Language?,
    displayTranslation: Boolean,
    arabicTextFont: ArabicTextFont,
    arabicTextFontSize: Int,
    reciter: Reciter?,
    onDisplayModeClick: (SurahVersePreferences.DisplayMode) -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onDisplayTranslationChange: (Boolean) -> Unit,
    onArabicTextFontChange: (ArabicTextFont) -> Unit,
    onIncreaseArabicTextFontSizeClick: () -> Unit,
    onDecreaseArabicTextFontSizeClick: () -> Unit,
    onDisplayTajweedChange: (Boolean) -> Unit,
    onReciterClick: () -> Unit,
    audioAutomaticScrolling: Boolean,
    onAutomaticScrollingChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        DisplaySection(
            displayMode = displayMode,
            onDisplayModeClick = onDisplayModeClick
        )
        HorizontalDivider()
        TextSection(
            translationLanguage = translationLanguage,
            displayTranslation = displayTranslation,
            arabicTextFont = arabicTextFont,
            arabicTextFontSize = arabicTextFontSize,
            displayTajweed = displayTajweed,
            onTranslationLanguageClick = onTranslationLanguageClick,
            onDisplayTranslationChange = onDisplayTranslationChange,
            onArabicTextFontChange = onArabicTextFontChange,
            onIncreaseArabicTextFontSizeClick = onIncreaseArabicTextFontSizeClick,
            onDecreaseArabicTextFontSizeClick = onDecreaseArabicTextFontSizeClick,
            onDisplayTajweedChange = onDisplayTajweedChange
        )
        HorizontalDivider()
        AudioSection(
            reciter = reciter,
            audioAutomaticScrolling = audioAutomaticScrolling,
            onReciterClick = onReciterClick,
            onAutomaticScrollingChange = onAutomaticScrollingChange
        )
    }
}

@Composable
fun DisplaySection(
    displayMode: SurahVersePreferences.DisplayMode,
    onDisplayModeClick: (SurahVersePreferences.DisplayMode) -> Unit
) {
    Column {
        SectionTitle(
            modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium),
            text = stringResource(R.string.display_mode)
        )

        Row(
            modifier = Modifier.padding(MaterialTheme.padding.medium),
            horizontalArrangement = Arrangement.mediumSpacing(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectableCell(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = { onDisplayModeClick(SurahVersePreferences.DisplayMode.LIST) }),
                selected = displayMode == SurahVersePreferences.DisplayMode.LIST,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_list_bulleted),
                        contentDescription = null
                    )
                },
                text = {
                    Text(text = stringResource(com.mfoumby.hassan.common.R.string.list))
                }
            )

            SelectableCell(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = { onDisplayModeClick(SurahVersePreferences.DisplayMode.PAGE) }),
                selected = displayMode == SurahVersePreferences.DisplayMode.PAGE,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_two_pager),
                        contentDescription = null
                    )
                },
                text = {
                    Text(text = stringResource(com.mfoumby.hassan.common.R.string.page))
                }
            )
        }
    }
}

@Composable
private fun TextSection(
    translationLanguage: Language?,
    displayTranslation: Boolean,
    arabicTextFont: ArabicTextFont,
    arabicTextFontSize: Int,
    displayTajweed: Boolean,
    onTranslationLanguageClick: () -> Unit,
    onDisplayTranslationChange: (Boolean) -> Unit,
    onArabicTextFontChange: (ArabicTextFont) -> Unit,
    onIncreaseArabicTextFontSizeClick: () -> Unit,
    onDecreaseArabicTextFontSizeClick: () -> Unit,
    onDisplayTajweedChange: (Boolean) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
       TranslationSectionPart(
           translationLanguage = translationLanguage,
           displayTranslation = displayTranslation,
           onTranslationLanguageClick = onTranslationLanguageClick,
           onDisplayTranslationChange = onDisplayTranslationChange
       )

        VerseSectionPart(
            arabicTextFont = arabicTextFont,
            arabicTextFontSize = arabicTextFontSize,
            displayTajweed = displayTajweed,
            onArabicTextFontChange = onArabicTextFontChange,
            onIncreaseArabicTextFontSizeClick = onIncreaseArabicTextFontSizeClick,
            onDecreaseArabicTextFontSizeClick = onDecreaseArabicTextFontSizeClick,
            onDisplayTajweedChange = onDisplayTajweedChange
        )
    }
}

@Composable
private fun TranslationSectionPart(
    translationLanguage: Language?,
    displayTranslation: Boolean,
    onTranslationLanguageClick: () -> Unit,
    onDisplayTranslationChange: (Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        SectionTitle(
            modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium),
            text = stringResource(com.mfoumby.hassan.common.R.string.translation)
        )

        Row(
            modifier = Modifier
                .clickable(onClick = onTranslationLanguageClick)
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.padding.medium,
                    vertical = MaterialTheme.padding.smallMedium
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.smallSpacing()
            ) {
                Text(
                    text = stringResource(com.mfoumby.hassan.common.R.string.select_translation),
                    style = MaterialTheme.typography.bodyMedium
                )

                translationLanguage?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.smallSpacing()
                    ) {
                        Image(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.bottom_sheet_image_size))
                                .clip(CircleShape),
                            painter = painterResource(translationLanguage.roundedFlagResId),
                            contentDescription = null
                        )

                        Text(
                            text = stringResource(translationLanguage.resId),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Icon(
                painter = painterResource(com.mfoumby.hassan.common.R.drawable.ic_outline_keyboard_arrow_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.padding.medium)
                .alpha(if (translationLanguage == null) 0.5f else 1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(com.mfoumby.hassan.common.R.string.display_translation),
                style = MaterialTheme.typography.bodyMedium
            )

            SimpleSwitch(
                checked = if (translationLanguage != null) displayTranslation else false,
                onCheckedChange = onDisplayTranslationChange,
                enabled = translationLanguage != null
            )
        }
    }
}

@Composable
private fun VerseSectionPart(
    arabicTextFont: ArabicTextFont,
    arabicTextFontSize: Int,
    displayTajweed: Boolean,
    onArabicTextFontChange: (ArabicTextFont) -> Unit,
    onIncreaseArabicTextFontSizeClick: () -> Unit,
    onDecreaseArabicTextFontSizeClick: () -> Unit,
    onDisplayTajweedChange: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        SectionTitle(text = stringResource(R.string.verse))

        Column(
            verticalArrangement = Arrangement.smallSpacing()
        ) {
            Text(
                text = stringResource(com.mfoumby.hassan.common.R.string.font),
                style = MaterialTheme.typography.bodyMedium
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.smallSpacing(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(ArabicTextFont.entries) {
                    SelectableCell(
                        modifier = Modifier.clickable(onClick = { onArabicTextFontChange(it) }),
                        selected = arabicTextFont == it,
                        icon = {
                            Text(
                                text = "بِسْمِ ٱللَّٰهِ",
                                style = arabicTextFont.typography,
                                fontSize = 20.sp
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(it.resId),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(com.mfoumby.hassan.common.R.string.font_size),
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.smallSpacing()
            ) {
                Icon(
                    modifier = Modifier.clickable(onClick = onDecreaseArabicTextFontSizeClick),
                    painter = painterResource(com.mfoumby.hassan.common.R.drawable.ic_outline_remove),
                    contentDescription = "Decrease font size"
                )

                Text(
                    text = arabicTextFontSize.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Icon(
                    modifier = Modifier.clickable(onClick = onIncreaseArabicTextFontSizeClick),
                    painter = painterResource(com.mfoumby.hassan.common.R.drawable.ic_outline_add),
                    contentDescription = "Increase font size"
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.display_tajweed),
                style = MaterialTheme.typography.bodyMedium
            )

            Switch(
                checked = displayTajweed,
                onCheckedChange = onDisplayTajweedChange
            )
        }
    }
}

@Composable
fun AudioSection(
    reciter: Reciter?,
    audioAutomaticScrolling: Boolean,
    onReciterClick: () -> Unit,
    onAutomaticScrollingChange: (Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        Column(
            verticalArrangement = Arrangement.smallSpacing()
        ) {
            SectionTitle(
                modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium),
                text = stringResource(R.string.reciter)
            )

            Row(
                modifier = Modifier
                    .clickable(onClick = onReciterClick)
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.padding.medium,
                        vertical = MaterialTheme.padding.smallMedium
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.smallSpacing()
                ) {
                    Text(
                        text = stringResource(R.string.select_reciter),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    reciter?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.smallSpacing()
                        ) {
                            SimpleAsyncImage(
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.bottom_sheet_image_size))
                                    .clip(CircleShape),
                                model = reciter.imageUrl,
                            )

                            Text(
                                text = reciter.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Icon(
                    painter = painterResource(com.mfoumby.hassan.common.R.drawable.ic_outline_keyboard_arrow_right),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.smallSpacing()
        ) {
            SectionTitle(
                modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium),
                text = stringResource(R.string.playback)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.padding.medium)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.automatic_scrolling),
                    style = MaterialTheme.typography.bodyMedium
                )

                SimpleSwitch(
                    checked = audioAutomaticScrolling,
                    onCheckedChange = onAutomaticScrollingChange
                )
            }
        }
    }
}

@Composable
private fun SelectableCell(
    modifier: Modifier = Modifier,
    selected: Boolean,
    shape: Shape = MaterialTheme.shapes.medium,
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit
) {
    val borderWidth = if (selected) 2.dp else 1.dp
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Box(
        modifier = modifier
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape
            )
            .padding(MaterialTheme.padding.medium),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.smallSpacing(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            text()
        }
    }
}

@PhonePreviews
@Composable
private fun SurahVerseSettingsBottomSheetContentPreview() {
    Previews.Preview {
        SurahVerseSettingsBottomSheetContent(
            displayMode = SurahVersePreferences.DisplayMode.LIST,
            displayTajweed = true,
            displayTranslation = true,
            arabicTextFont = ArabicTextFont.UTHMANIC,
            arabicTextFontSize = 16,
            translationLanguage = Language.ENGLISH,
            reciter = reciterFixture,
            audioAutomaticScrolling = true,
            onDisplayModeClick = {},
            onArabicTextFontChange = {},
            onIncreaseArabicTextFontSizeClick = {},
            onDecreaseArabicTextFontSizeClick = {},
            onDisplayTajweedChange = {},
            onTranslationLanguageClick = {},
            onDisplayTranslationChange = {},
            onReciterClick = {},
            onAutomaticScrollingChange = {}
        )
    }
}