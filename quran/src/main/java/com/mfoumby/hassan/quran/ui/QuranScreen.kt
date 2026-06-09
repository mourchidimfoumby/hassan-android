package com.mfoumby.hassan.quran.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.components.TitleTopBar
import com.mfoumby.hassan.common.ui.components.VerticalScrollBarIndicator
import com.mfoumby.hassan.common.ui.theme.HassanTheme
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.surahFixture
import com.mfoumby.hassan.quran.domain.surahFixtures
import org.koin.androidx.compose.koinViewModel

@Composable
fun QuranDestination(
    bottomBar: @Composable () -> Unit,
    onSurahClick: (Int) -> Unit,
    viewModel: QuranViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.surahs != null) {
        QuranScreen(
            surahs = uiState.surahs!!,
            bottomBar = bottomBar,
            onSurahClick = onSurahClick
        )
    }
}

@Composable
private fun QuranScreen(
    surahs: List<Surah>,
    bottomBar: @Composable () -> Unit,
    onSurahClick: (Int) -> Unit
) {
    Scaffold(
        topBar = { TitleTopBar(title = stringResource(R.string.quran)) },
        bottomBar = bottomBar
    ) { innerPadding ->
        val listState = rememberLazyListState()
        Box(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(state = listState) {
                items(surahs.size) { index ->
                    val surah = surahs[index]
                    if (index == 0) {
                        HorizontalDivider()
                    }
                    SurahCell(
                        surah = surah,
                        onClick = { onSurahClick(surah.number) }
                    )
                    HorizontalDivider()
                }
            }

            VerticalScrollBarIndicator(
                modifier = Modifier.align(Alignment.CenterEnd),
                state = listState,
                itemsCount = surahs.size
            )
        }
    }
}

@Composable
private fun SurahCell(surah: Surah, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = { Text(text = surah.transliteration) },
        leadingContent = { Text(text = surah.number.toString()) },
        supportingContent = { Text(text = surah.translation) },
    )
}

@PhonePreviews
@Composable
private fun QuranScreenPreview() {
    HassanTheme {
        QuranScreen(
            surahs = surahFixtures,
            bottomBar = {},
            onSurahClick = {}
        )
    }
}

@Preview
@Composable
private fun SurahCellPreview() {
    HassanTheme {
        SurahCell(
            surah = surahFixture,
            onClick = {}
        )
    }
}