package com.mfoumby.hassan.quran.ui.quran

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mfoumby.hassan.common.ui.theme.transparentListItemColor
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.ui.SurahMetadata

@Composable
fun SurahListItem(
    surah: Surah,
    modifier: Modifier = Modifier,
    colors: ListItemColors = MaterialTheme.colorScheme.transparentListItemColor
) {
    ListItem(
        modifier = modifier,
        headlineContent = { Text(surah.transliteration) },
        leadingContent = { Text(surah.number.toString()) },
        supportingContent = { Text(surah.translation) },
        trailingContent = {
            Icon(
                painter = painterResource(SurahMetadata.getSurahImageResId(surah.number)),
                contentDescription = null
            )
        },
        colors = colors
    )
}

@Composable
fun JuzListItem(
    surahVerse: SurahVerse,
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = { Text(surahVerse.verse.juzNumber.toString()) }
) {
    ListItem(
        modifier = modifier,
        headlineContent = { Text("${stringResource(R.string.juz)} ${surahVerse.verse.juzNumber}") },
        leadingContent = leadingContent,
        supportingContent = { Text("${surahVerse.surah.transliteration} - ${stringResource(R.string.verse)} ${surahVerse.verse.verseNumber}") },
        colors = MaterialTheme.colorScheme.transparentListItemColor
    )
}

@Composable
fun HizbListItem(
    surahVerse: SurahVerse,
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = { Text(surahVerse.verse.hizbNumber.toString()) }
) {
    ListItem(
        modifier = modifier,
        headlineContent = { Text("${stringResource(R.string.hizb)} ${surahVerse.verse.hizbNumber}") },
        leadingContent = leadingContent,
        supportingContent = { Text("${surahVerse.surah.transliteration} - ${stringResource(R.string.verse)} ${surahVerse.verse.verseNumber}") },
        colors = MaterialTheme.colorScheme.transparentListItemColor
    )
}

@Composable
fun SurahBookmarkCard(
    surahVerse: SurahVerse,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        ListItem(
            headlineContent = { Text(surahVerse.surah.transliteration) },
            supportingContent = {
                Text("${stringResource(R.string.verse)} ${surahVerse.verse.verseNumber}")
            },
            trailingContent = {
                Icon(
                    painter = painterResource(SurahMetadata.getSurahImageResId(surahVerse.surah.number)),
                    contentDescription = null
                )
            },
            colors = MaterialTheme.colorScheme.transparentListItemColor
        )
    }
}

@Composable
fun JuzBookmarkCard(
    surahVerse: SurahVerse,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        JuzListItem(
            surahVerse = surahVerse,
            leadingContent = null
        )
    }
}

@Composable
fun HizbBookmarkCard(
    surahVerse: SurahVerse,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        HizbListItem(
            surahVerse = surahVerse,
            leadingContent = null
        )
    }
}