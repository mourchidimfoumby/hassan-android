package com.mfoumby.hassan.quran.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mfoumby.hassan.common.domain.extension.toIndex
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.TitleTopBar
import com.mfoumby.hassan.common.ui.components.VerticalScrollBarIndicator
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.juzFixture
import com.mfoumby.hassan.quran.domain.juzFixtures
import com.mfoumby.hassan.quran.domain.surahFixture
import com.mfoumby.hassan.quran.domain.surahFixtures
import org.koin.androidx.compose.koinViewModel

@Composable
fun QuranDestination(
    bottomBar: @Composable () -> Unit,
    onSurahClick: (Int) -> Unit,
    onJuzClick: (Int) -> Unit,
    viewModel: QuranViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (!uiState.initializing) {
        QuranScreen(
            bottomBar = bottomBar,
            surahs = uiState.surahs!!,
            allJuz = uiState.allJuz!!,
            tab = uiState.tab,
            onTabClick = viewModel::onTabChange,
            onSurahClick = onSurahClick,
            onJuzClick = onJuzClick
        )
    }
}

@Composable
private fun QuranScreen(
    bottomBar: @Composable () -> Unit,
    surahs: List<Surah>,
    allJuz: List<Juz>,
    tab: QuranViewModel.QuranTab,
    onTabClick: (QuranViewModel.QuranTab) -> Unit,
    onSurahClick: (Int) -> Unit,
    onJuzClick: (Int) -> Unit
) {
    Scaffold(
        topBar = { TitleTopBar(title = stringResource(R.string.quran)) },
        bottomBar = bottomBar
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            PrimaryTabRow(selectedTabIndex = tab.ordinal) {
                QuranViewModel.QuranTab.entries.forEachIndexed { index, item ->
                    Tab(
                        selected = tab.ordinal == index,
                        onClick = { onTabClick(item) },
                        text = {
                            Text(
                                when (item) {
                                    QuranViewModel.QuranTab.SURAH -> stringResource(R.string.surah).uppercase()
                                    QuranViewModel.QuranTab.JUZ -> stringResource(R.string.juz).uppercase()
                                }
                            )
                        }
                    )
                }
            }

            when (tab) {
                QuranViewModel.QuranTab.SURAH -> {
                    SurahTabContent(
                        modifier = Modifier.weight(1f),
                        surahs = surahs,
                        onSurahClick = { onSurahClick(it.number) },
                    )
                }

                QuranViewModel.QuranTab.JUZ -> {
                    JuzTabContent(
                        modifier = Modifier.weight(1f),
                        allJuz = allJuz,
                        onJuzClick = { onJuzClick(it.number) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SurahTabContent(
    modifier: Modifier = Modifier,
    surahs: List<Surah>,
    onSurahClick: (Surah) -> Unit
) {
    val listState = rememberLazyListState()
    Box(modifier = modifier) {
        LazyColumn(state = listState) {
            items(surahs.size) { index ->
                val surah = surahs[index]
                SurahCell(
                    surah = surah,
                    onSurahClick = onSurahClick
                )
                if (index < surahs.size.toIndex()) {
                    HorizontalDivider()
                }
            }
        }

        VerticalScrollBarIndicator(
            modifier = Modifier.align(Alignment.CenterEnd),
            state = listState,
            itemsCount = surahs.size
        )
    }
}

@Composable
fun SurahCell(
    surah: Surah,
    onSurahClick: (Surah) -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = { onSurahClick(surah) }),
        headlineContent = { Text(text = surah.transliteration) },
        leadingContent = { Text(text = surah.number.toString()) },
        supportingContent = { Text(text = surah.translation) },
    )
}

@Composable
private fun JuzTabContent(
    modifier: Modifier = Modifier,
    allJuz: List<Juz>,
    onJuzClick: (Juz) -> Unit
) {
    val listState = rememberLazyListState()
    Box(modifier = modifier) {
        LazyColumn(state = listState) {
            items(allJuz.size) { index ->
                JuzCell(
                    juz = allJuz[index],
                    onJuzClick = onJuzClick
                )
                if (index < allJuz.size.toIndex()) {
                    HorizontalDivider()
                }
            }
        }

        VerticalScrollBarIndicator(
            modifier = Modifier.align(Alignment.CenterEnd),
            state = listState,
            itemsCount = allJuz.size
        )
    }
}

@Composable
fun JuzCell(
    juz: Juz,
    onJuzClick: (Juz) -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = { onJuzClick(juz) }),
        headlineContent = { Text(text = "${stringResource(R.string.juz)} ${juz.number}") },
        leadingContent = { Text(text = juz.number.toString()) },
        supportingContent = { Text(text = "${juz.firstSurahVerse.surah.transliteration} - ${stringResource(R.string.verse)} ${juz.firstSurahVerse.verse.verseNumber}") },
    )
}

@PhonePreviews
@Composable
private fun SurahCellPreview() {
    Previews.Preview {
        SurahCell(
            surah = surahFixture,
            onSurahClick = {}
        )
    }
}

@PhonePreviews
@Composable
private fun JuzCellPreview() {
    Previews.Preview {
        JuzCell(
            juz = juzFixture,
            onJuzClick = {}
        )
    }
}

@PhonePreviews
@Composable
private fun QuranScreenPreview() {
    Previews.Preview {
        QuranScreen(
            bottomBar = {},
            surahs = surahFixtures,
            allJuz = juzFixtures,
            tab = QuranViewModel.QuranTab.SURAH,
            onTabClick = {},
            onSurahClick = {},
            onJuzClick = {}
        )
    }
}