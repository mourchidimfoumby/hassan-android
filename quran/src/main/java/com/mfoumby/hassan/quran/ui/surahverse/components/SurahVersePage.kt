package com.mfoumby.hassan.quran.ui.surahverse.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mfoumby.hassan.common.domain.NumberFormatUtils
import com.mfoumby.hassan.common.domain.extension.half
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.BismillahText
import com.mfoumby.hassan.common.ui.extension.mediumSpacing
import com.mfoumby.hassan.common.ui.theme.bodyUthmanic
import com.mfoumby.hassan.common.ui.theme.padding
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.surahFixture
import com.mfoumby.hassan.quran.domain.surahVerseFixtures
import com.mfoumby.hassan.quran.ui.surahverse.ScrollValue

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SurahVersePage(
    modifier: Modifier = Modifier,
    surahVerses: List<SurahVerse>,
    surahVerseToScroll: SurahVerse?,
    onSurahVerseClick: (SurahVerse) -> Unit,
    onScrollValueChange: (ScrollValue) -> Unit
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        snapshotFlow { scrollState.value }
            .collect {
                onScrollValueChange(ScrollValue(it, scrollState.maxValue))
            }
    }

    LaunchedEffect(Unit) {
        surahVerseToScroll?.let {
            if (surahVerses.indexOf(it) > surahVerses.size.half()) {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        surahVerses.firstOrNull()?.verse?.let { firstVerse ->
            Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium),
                text = "Juz ${firstVerse.juz} | Page ${firstVerse.page}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(
            modifier = Modifier.verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            surahVerses.groupBy { it.surah.number }.forEach { groupedSurahVerses ->
                val first = groupedSurahVerses.value.first()
                if (first.verse.verseNumber == 1) {
                    SurahHeader(surahName = first.surah.name)
                    BismillahText(modifier = Modifier.fillMaxWidth())
                }

                SurahPageText(
                    surahVerses = groupedSurahVerses.value,
                    onSurahVerseClick = onSurahVerseClick
                )
            }
        }
    }
}

@Composable
private fun SurahHeader(surahName: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                modifier = Modifier.size(
                    width = 200.dp,
                    height = 50.dp
                ),
                painter = painterResource(R.drawable.surah_outline),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )

            Text(
                text = surahName,
                style = MaterialTheme.typography.bodyUthmanic
            )
        }
    }
}

@Composable
fun SurahPageText(
    surahVerses: List<SurahVerse>,
    onSurahVerseClick: (SurahVerse) -> Unit
) {
    val clickTextStyle = SpanStyle(
        background = Color.Gray.copy(alpha = 0.3f)
    )
    val annotatedText = buildAnnotatedString {
        surahVerses.forEach { surahVerse ->
            withLink(
                LinkAnnotation.Clickable(
                    tag = surahVerse.verse.verseNumber.toString(),
                    styles = TextLinkStyles(
                        pressedStyle = clickTextStyle
                    ),
                    linkInteractionListener = {
                        onSurahVerseClick(surahVerse)
                    }
                )
            ) {
                withStyle(
                    style = MaterialTheme.typography.bodyUthmanic
                        .toSpanStyle()
                        .copy(color = MaterialTheme.colorScheme.onSurface,)
                ) {
                    append(surahVerse.verse.text)
                    append(" ")
                    append(NumberFormatUtils.toArabic(surahVerse.verse.verseNumber))
                }
            }
            append("\u2009")
        }
    }

    BasicText(
        modifier = Modifier.padding(MaterialTheme.padding.medium),
        text = annotatedText,
        style = MaterialTheme.typography.bodyUthmanic.copy(
            lineHeight = MaterialTheme.typography.bodyUthmanic.lineHeight * 1.2
        )
    )
}

@PhonePreviews
@Composable
private fun SurahVersePagePreview() {
    Previews.Preview {
        SurahVersePage(
            surahVerses = surahVerseFixtures,
            surahVerseToScroll = null,
            onSurahVerseClick = {},
            onScrollValueChange = {}
        )
    }
}

@PhonePreviews
@Composable
private fun SurahHeaderPreview() {
    Previews.Preview {
        SurahHeader(surahName = surahFixture.name)
    }
}
