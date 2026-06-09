package com.mfoumby.hassan.quran.ui.surahverse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.mfoumby.hassan.common.domain.NumberFormatUtils
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.components.BackTopBar
import com.mfoumby.hassan.common.ui.components.VerticalScrollBarIndicator
import com.mfoumby.hassan.common.ui.theme.HassanTheme
import com.mfoumby.hassan.common.ui.theme.UthmanicHafsFamily
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.surahFixture
import com.mfoumby.hassan.quran.domain.surahVerseFixtures
import com.upsaclay.common.presentation.theme.padding
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SurahVerseDestination(
    surahNumber: Int,
    onBackClick: () -> Unit,
    viewModel: SurahVerseViewModel = koinViewModel(
        parameters = { parametersOf(surahNumber) }
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.surah != null) {
        SurahVerseScreen(
            surah = uiState.surah!!,
            surahVerses = uiState.surahVerses,
            arabicFontSize = uiState.arabicFontSize,
            onBackClick = onBackClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SurahVerseScreen(
    surah: Surah,
    surahVerses: List<SurahVerse>,
    arabicFontSize: Int,
    onBackClick: () -> Unit
) {
    val listState = rememberLazyListState()
    Scaffold(
        topBar = { BackTopBar(onBackClick = onBackClick, title = surah.transliteration) }
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
                        arabicFontSize = arabicFontSize
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
}

@Composable
private fun SurahVerseCell(
    modifier: Modifier = Modifier,
    surahVerse: SurahVerse,
    arabicFontSize: Int
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.padding.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.smallMedium)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = surahVerse.text + " " + NumberFormatUtils.toArabic(surahVerse.number),
            fontFamily = UthmanicHafsFamily,
            textAlign = TextAlign.Right,
            fontSize = arabicFontSize.sp,
            lineHeight = (arabicFontSize + MaterialTheme.padding.medium.value).sp
        )

        surahVerse.translation?.let {
            Text(text = surahVerse.number.toString() + ". " + it)
        }
    }
}

@PhonePreviews
@Composable
private fun SurahVerseScreenPreview() {
    HassanTheme {
        SurahVerseScreen(
            surah = surahFixture,
            surahVerses = surahVerseFixtures,
            arabicFontSize = 22,
            onBackClick = {}
        )
    }
}