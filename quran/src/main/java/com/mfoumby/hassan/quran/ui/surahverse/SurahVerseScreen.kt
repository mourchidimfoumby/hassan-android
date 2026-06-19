package com.mfoumby.hassan.quran.ui.surahverse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mfoumby.hassan.common.domain.NumberFormatUtils
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.BackTopBar
import com.mfoumby.hassan.common.ui.components.VerticalScrollBarIndicator
import com.mfoumby.hassan.common.ui.theme.bodyUthmanic
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.surahFixture
import com.mfoumby.hassan.quran.domain.surahVerseFixtures
import com.mfoumby.hassan.quran.domain.surahVersePreferencesFixture
import com.mfoumby.hassan.quran.ui.surahverse.components.SurahVerseBottomSheet
import com.upsaclay.common.presentation.theme.padding
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SurahVerseDestination(
    surahNumber: Int,
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit,
    viewModel: SurahVerseViewModel = koinViewModel(
        parameters = { parametersOf(surahNumber) }
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    if (!uiState.initializing) {
        SurahVerseScreen(
            surah = uiState.surah!!,
            surahVerses = uiState.surahVerses,
            surahVersePreferences = uiState.surahVersePreferences!!,
            onBackClick = onBackClick,
            onTranslationLanguageClick = onTranslationLanguageClick,
            onDisplayTranslationChange = viewModel::onDisplayTranslationChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SurahVerseScreen(
    surah: Surah,
    surahVerses: List<SurahVerse>,
    surahVersePreferences: SurahVersePreferences,
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onDisplayTranslationChange: (Boolean) -> Unit
) {
    val listState = rememberLazyListState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = surah.transliteration,
                actions = {
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Show settings"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(state = listState) {
                items(surahVerses.size) { index ->
                    val surahVerse = surahVerses[index]
                    if (index == 0) {
                        HorizontalDivider()
                    }
                    SurahVerseCell(
                        surahVerse = surahVerse,
                        displayTranslation = surahVersePreferences.displayTranslation
                    )
                    HorizontalDivider()
                }
            }

            VerticalScrollBarIndicator(
                modifier = Modifier.align(Alignment.CenterEnd),
                state = listState,
                itemsCount = surahVerses.size
            )
        }
    }

    if (showBottomSheet) {
        SurahVerseBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            translationLanguage = surahVersePreferences.translationLanguage,
            onTranslationLanguageClick = {
                showBottomSheet = false
                onTranslationLanguageClick()
            },
            displayTranslation = surahVersePreferences.displayTranslation,
            onDisplayTranslationChange = onDisplayTranslationChange
        )
    }
}

@Composable
private fun SurahVerseCell(
    surahVerse: SurahVerse,
    displayTranslation: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.padding.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.smallMedium)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = surahVerse.text + " " + NumberFormatUtils.toArabic(surahVerse.number),
            style = MaterialTheme.typography.bodyUthmanic
        )

        if (displayTranslation) {
            surahVerse.translation?.let {
                Text(text = surahVerse.number.toString() + ". " + it)
            }
        }
    }
}


@PhonePreviews
@Composable
private fun SurahVerseScreenPreview() {
    Previews.Preview {
        SurahVerseScreen(
            surah = surahFixture,
            surahVerses = surahVerseFixtures,
            surahVersePreferences = surahVersePreferencesFixture,
            onBackClick = {},
            onTranslationLanguageClick = {},
            onDisplayTranslationChange = {}
        )
    }
}