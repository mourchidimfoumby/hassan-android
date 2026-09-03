package com.mfoumby.hassan.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mfoumby.hassan.common.R
import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage.TranslationLanguageState
import com.mfoumby.hassan.common.extension.mediumSpacing
import com.mfoumby.hassan.common.extension.smallSpacing
import com.mfoumby.hassan.common.getRoundedFlagResId
import com.mfoumby.hassan.common.getStringResId
import com.mfoumby.hassan.common.ui.components.SimpleLazyColumn
import com.mfoumby.hassan.common.ui.theme.padding

@Composable
fun TranslationLanguageListComponent(
    modifier: Modifier = Modifier,
    selectedLanguage: Language?,
    translationLanguages: List<TranslationLanguage>,
    onTranslationLanguageSelect: (TranslationLanguage) -> Unit,
    onDeleteTranslationLanguageClick: (TranslationLanguage) -> Unit,
    onCancelTranslationLanguageDownloadClick: (TranslationLanguage) -> Unit
) {
    SimpleLazyColumn(
        modifier = modifier,
        itemCount = translationLanguages.size
    ) {
        items(translationLanguages) { translationLanguage ->
            TranslationLanguageItem(
                translationLanguage = translationLanguage,
                selected = translationLanguage.language == selectedLanguage,
                onClick = { onTranslationLanguageSelect(translationLanguage) },
                onDeleteClick = { onDeleteTranslationLanguageClick(translationLanguage) },
                onCancelDownloadClick = { onCancelTranslationLanguageDownloadClick(translationLanguage) }
            )
        }
    }
}

@Composable
private fun TranslationLanguageItem(
    translationLanguage: TranslationLanguage,
    selected: Boolean,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCancelDownloadClick: () -> Unit
) {
    val containerColor = if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(containerColor)
            .padding(MaterialTheme.padding.medium),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        Row(
            horizontalArrangement = Arrangement.mediumSpacing(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(translationLanguage.language.getRoundedFlagResId()),
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(R.dimen.flag_size))
            )

            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(translationLanguage.language.getStringResId())
            )

            when (translationLanguage.state) {
                TranslationLanguageState.Downloaded -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.smallSpacing()
                    ) {
                        if (selected) {
                            Icon(
                                painter = painterResource(R.drawable.ic_fill_check_circle),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.ic_outline_circle),
                                tint = MaterialTheme.colorScheme.outlineVariant,
                                contentDescription = null
                            )
                        }

                        Icon(
                            modifier = Modifier.clickable(onClick = onDeleteClick),
                            painter = painterResource(R.drawable.ic_outline_delete),
                            contentDescription = "Delete translation language",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }

                TranslationLanguageState.NotDownloaded -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_download),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                is TranslationLanguageState.Downloading -> {
                    Icon(
                        modifier = Modifier.clickable(onClick = onCancelDownloadClick),
                        painter = painterResource(R.drawable.ic_outline_close),
                        contentDescription = "Cancel downloading",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        (translationLanguage.state as? TranslationLanguageState.Downloading)?.let { downloadingState ->
            Column(verticalArrangement = Arrangement.smallSpacing()) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress =  { downloadingState.progress }
                )

                Text(
                    text = "${(downloadingState.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@PhonePreviews
@Composable
private fun TranslationLanguageListComponentPreview() {
    Previews.Preview {
        TranslationLanguageListComponent(
            selectedLanguage = Language.ENGLISH,
            translationLanguages = listOf(
                TranslationLanguage(Language.ENGLISH, TranslationLanguageState.Downloaded),
                TranslationLanguage(Language.FRENCH, TranslationLanguageState.Downloaded),
                TranslationLanguage(Language.FRENCH, TranslationLanguageState.NotDownloaded),
                TranslationLanguage(Language.ARABIC, TranslationLanguageState.Downloading(0.1f))
            ),
            onTranslationLanguageSelect = {},
            onDeleteTranslationLanguageClick = {},
            onCancelTranslationLanguageDownloadClick = {}
        )
    }
}