package com.mfoumby.hassan.quran.ui.surahverse.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.common.getRoundedFlagResId
import com.mfoumby.hassan.common.getStringResId
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.SectionTitle
import com.mfoumby.hassan.common.ui.components.SimpleAsyncImage
import com.mfoumby.hassan.common.ui.extension.mediumSpacing
import com.mfoumby.hassan.common.ui.extension.smallSpacing
import com.mfoumby.hassan.common.ui.theme.padding
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.reciterFixture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahVerseSettingsBottomSheet(
    onDismissRequest: () -> Unit,
    displayTranslation: Boolean,
    translationLanguage: Language?,
    reciter: Reciter?,
    onTranslationLanguageClick: () -> Unit,
    onDisplayTranslationChange: (Boolean) -> Unit,
    onReciterClick: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            TranslationSection(
                translationLanguage = translationLanguage,
                onTranslationLanguageClick = onTranslationLanguageClick,
                displayTranslation = displayTranslation,
                onDisplayTranslationChange = onDisplayTranslationChange
            )
            ReciterSection(
                reciter = reciter,
                onReciterClick = onReciterClick
            )
        }
    }
}

@Composable
private fun TranslationSection(
    translationLanguage: Language?,
    onTranslationLanguageClick: () -> Unit,
    displayTranslation: Boolean,
    onDisplayTranslationChange: (Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        SectionTitle(
            modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium),
            text = stringResource(com.mfoumby.hassan.common.R.string.translation)
        )

        Column {
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

                Switch(
                    checked = if (translationLanguage != null) displayTranslation else false,
                    onCheckedChange = onDisplayTranslationChange,
                    enabled = translationLanguage != null
                )
            }

            Row(
                modifier = Modifier
                    .clickable(onClick = onTranslationLanguageClick)
                    .fillMaxWidth()
                    .padding(MaterialTheme.padding.medium),
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
                                painter = painterResource(translationLanguage.getRoundedFlagResId()),
                                contentDescription = null
                            )

                            Text(
                                text = stringResource(translationLanguage.getStringResId()),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ReciterSection(
    reciter: Reciter?,
    onReciterClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        SectionTitle(
            modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium),
            text = stringResource(R.string.reciter)
        )

        Column {
            Row(
                modifier = Modifier
                    .clickable(onClick = onReciterClick)
                    .fillMaxWidth()
                    .padding(MaterialTheme.padding.medium),
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
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@PhonePreviews
@Composable
private fun SurahVerseSettingsBottomSheetPreview() {
    Previews.Preview {
        SurahVerseSettingsBottomSheet(
            onDismissRequest = {},
            displayTranslation = true,
            translationLanguage = Language.ENGLISH,
            reciter = reciterFixture,
            onTranslationLanguageClick = {},
            onDisplayTranslationChange = {},
            onReciterClick = {}
        )
    }
}