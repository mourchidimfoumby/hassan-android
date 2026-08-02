package com.mfoumby.hassan.quran.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mfoumby.hassan.common.domain.extension.asIndex
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.TitleTopBar
import com.mfoumby.hassan.common.ui.components.VerticalScrollBarIndicator
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.Hizb
import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.hizbFixtures
import com.mfoumby.hassan.quran.domain.juzFixtures
import com.mfoumby.hassan.quran.domain.surahFixtures
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun QuranDestination(
    bottomBar: @Composable () -> Unit,
    onSurahClick: (Int) -> Unit,
    onJuzClick: (Int) -> Unit,
    onHizbClick: (Int) -> Unit,
    viewModel: QuranViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (!uiState.initializing) {
        QuranScreen(
            bottomBar = bottomBar,
            surahs = uiState.surahs!!,
            allJuz = uiState.allJuz!!,
            allHizb = uiState.allHizb!!,
            onSurahClick = onSurahClick,
            onJuzClick = onJuzClick,
            onHizbClick = onHizbClick
        )
    }
}

@Composable
private fun QuranScreen(
    bottomBar: @Composable () -> Unit,
    surahs: List<Surah>,
    allJuz: List<Juz>,
    allHizb: List<Hizb>,
    onSurahClick: (Int) -> Unit,
    onJuzClick: (Int) -> Unit,
    onHizbClick: (Int) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = QuranTab.SURAH.ordinal,
        pageCount = { QuranTab.entries.size }
    )
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TitleTopBar(title = stringResource(R.string.quran)) },
        bottomBar = bottomBar
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
                QuranTab.entries.forEachIndexed { index, item ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    page = index,
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessLow,
                                    )
                                )
                            }
                        },
                        text = {
                            Text(
                                text = when (item) {
                                    QuranTab.SURAH -> stringResource(R.string.surah)
                                    QuranTab.JUZ -> stringResource(R.string.juz)
                                    QuranTab.HIZB -> stringResource(R.string.hizb)
                                }.uppercase()
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                modifier = Modifier.weight(1f),
                state = pagerState
            ) { page ->
                when (QuranTab.entries[page]) {
                    QuranTab.SURAH -> {
                        SurahTabContent(
                            modifier = Modifier.fillMaxSize(),
                            surahs = surahs,
                            onSurahClick = { onSurahClick(it.number) },
                        )
                    }

                    QuranTab.JUZ -> {
                        JuzTabContent(
                            modifier = Modifier.fillMaxSize(),
                            allJuz = allJuz,
                            onJuzClick = { onJuzClick(it.number) },
                        )
                    }

                    QuranTab.HIZB -> {
                        HizbTabContent(
                            modifier = Modifier.fillMaxSize(),
                            allHizb = allHizb,
                            onHizbClick = { onHizbClick(it.number) },
                        )
                    }
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
                ListItem(
                    modifier = Modifier.clickable(onClick = { onSurahClick(surah) }),
                    headlineContent = { Text(text = surah.transliteration) },
                    leadingContent = { Text(text = surah.number.toString()) },
                    supportingContent = { Text(text = surah.translation) },
                )
                if (index < surahs.size.asIndex()) {
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
private fun JuzTabContent(
    modifier: Modifier = Modifier,
    allJuz: List<Juz>,
    onJuzClick: (Juz) -> Unit
) {
    val listState = rememberLazyListState()
    Box(modifier = modifier) {
        LazyColumn(state = listState) {
            items(allJuz.size) { index ->
                val juz = allJuz[index]
                ListItem(
                    modifier = Modifier.clickable(onClick = { onJuzClick(juz) }),
                    headlineContent = { Text(text = "${stringResource(R.string.juz)} ${juz.number}") },
                    leadingContent = { Text(text = juz.number.toString()) },
                    supportingContent = { Text(text = "${juz.firstSurahVerse.surah.transliteration} - ${stringResource(R.string.verse)} ${juz.firstSurahVerse.verse.verseNumber}") },
                )
                if (index < allJuz.size.asIndex()) {
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
private fun HizbTabContent(
    modifier: Modifier = Modifier,
    allHizb: List<Hizb>,
    onHizbClick: (Hizb) -> Unit
) {
    val listState = rememberLazyListState()
    Box(modifier = modifier) {
        LazyColumn(state = listState) {
            items(allHizb.size) { index ->
                val hizb = allHizb[index]
                ListItem(
                    modifier = Modifier.clickable(onClick = { onHizbClick(hizb) }),
                    headlineContent = { Text(text = "${stringResource(R.string.hizb)} ${hizb.number}") },
                    leadingContent = { Text(text = hizb.number.toString()) },
                    supportingContent = { Text(text = "${hizb.firstSurahVerse.surah.transliteration} - ${stringResource(R.string.verse)} ${hizb.firstSurahVerse.verse.verseNumber}") },
                )
                if (index < allHizb.size.asIndex()) {
                    HorizontalDivider()
                }
            }
        }

        VerticalScrollBarIndicator(
            modifier = Modifier.align(Alignment.CenterEnd),
            state = listState,
            itemsCount = allHizb.size
        )
    }
}

private enum class QuranTab {
    SURAH, JUZ, HIZB
}

@PhonePreviews
@Composable
private fun QuranScreenPreview() {
    Previews.Preview {
        QuranScreen(
            bottomBar = {},
            surahs = surahFixtures,
            allJuz = juzFixtures,
            allHizb = hizbFixtures,
            onSurahClick = {},
            onJuzClick = {},
            onHizbClick = {}
        )
    }
}

@PhonePreviews
@Composable
private fun SurahTabContentPreview() {
    Previews.Preview {
        SurahTabContent(
            surahs = surahFixtures,
            onSurahClick = {}
        )
    }
}

@PhonePreviews
@Composable
private fun JuzTabContentPreview() {
    Previews.Preview {
        JuzTabContent(
            allJuz = juzFixtures,
            onJuzClick = {}
        )
    }
}

@PhonePreviews
@Composable
private fun HizbTabContentPreview() {
    Previews.Preview {
        HizbTabContent(
            allHizb = hizbFixtures,
            onHizbClick = {}
        )
    }
}