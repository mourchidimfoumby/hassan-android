package com.mfoumby.hassan.quran.ui.surahverse.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mfoumby.hassan.common.domain.NumberFormatUtils
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.VerticalScrollBarIndicator
import com.mfoumby.hassan.common.ui.theme.bodyUthmanic
import com.mfoumby.hassan.common.ui.theme.padding
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation
import com.mfoumby.hassan.quran.domain.surahVerseFixtures
import com.mfoumby.hassan.quran.domain.surahVersePreferencesFixture
import com.mfoumby.hassan.quran.domain.surahVerseTranslationFixtures
import com.mfoumby.hassan.quran.ui.surahverse.ScrollValue
import kotlin.math.max

@Composable
fun SurahVerseList(
    modifier: Modifier = Modifier,
    surahVerses: List<SurahVerse>,
    surahVerseTranslations: List<SurahVerseTranslation>,
    surahVersePreferences: SurahVersePreferences,
    surahVerseToScroll: SurahVerse?,
    onSurahVerseClick: (SurahVerse) -> Unit,
    onScrollValueChange: (ScrollValue) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.collect {
            onScrollValueChange(ScrollValue(it, surahVerses.size))
        }
    }

    LaunchedEffect(Unit) {
        surahVerseToScroll?.let {
            val index = max(surahVerses.indexOf(it), 0)
            listState.animateScrollToItem(index)
        }
    }

    Box(modifier = modifier) {
        LazyColumn(state = listState) {
            items(surahVerses.size) { index ->
                val surahVerse = surahVerses[index]
                if (surahVerse.verse.verseNumber == 1) {
                    SurahHeader(surah = surahVerse.surah)
                    if (surahVerse.verse.verseNumber != 1) {
                        HorizontalDivider()
                    }
                }

                SurahVerseCell(
                    surahVerse = surahVerse,
                    surahVerseTranslation = surahVerseTranslations.getOrNull(index),
                    displayTranslation = surahVersePreferences.displayTranslation,
                    onClick = { onSurahVerseClick(surahVerse) }
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

@Composable
private fun SurahVerseCell(
    surahVerse: SurahVerse,
    surahVerseTranslation: SurahVerseTranslation?,
    displayTranslation: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(MaterialTheme.padding.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.smallMedium)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = surahVerse.verse.text + " " + NumberFormatUtils.toArabic(surahVerse.verse.verseNumber),
            style = MaterialTheme.typography.bodyUthmanic
        )

        if (displayTranslation) {
            surahVerseTranslation?.let {
                Text(text = "${it.verseNumber}. ${it.text}")
            }
        }
    }
}

@PhonePreviews
@Composable
private fun SurahVerseListPreview() {
    Previews.Preview {
        SurahVerseList(
            surahVerses = surahVerseFixtures,
            surahVerseTranslations = surahVerseTranslationFixtures,
            surahVersePreferences = surahVersePreferencesFixture,
            surahVerseToScroll = null,
            onSurahVerseClick = {},
            onScrollValueChange = {}
        )
    }
}