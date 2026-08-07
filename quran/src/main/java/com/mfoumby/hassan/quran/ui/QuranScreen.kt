package com.mfoumby.hassan.quran.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mfoumby.hassan.common.domain.extension.asIndex
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.SectionTitle
import com.mfoumby.hassan.common.ui.components.TitleTopBar
import com.mfoumby.hassan.common.ui.components.VerticalScrollBarIndicator
import com.mfoumby.hassan.common.ui.extension.smallSpacing
import com.mfoumby.hassan.common.ui.theme.padding
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.HizbNumber
import com.mfoumby.hassan.quran.domain.JuzNumber
import com.mfoumby.hassan.quran.domain.SurahNumber
import com.mfoumby.hassan.quran.domain.VerseNumber
import com.mfoumby.hassan.quran.domain.entity.Hizb
import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.hizbFixtures
import com.mfoumby.hassan.quran.domain.juzFixtures
import com.mfoumby.hassan.quran.domain.surahFixtures
import com.mfoumby.hassan.quran.domain.surahVerseFixture
import com.mfoumby.hassan.quran.domain.surahVersePreferencesFixture
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun QuranDestination(
    bottomBar: @Composable () -> Unit,
    onSurahClick: (SurahNumber) -> Unit,
    onJuzClick: (JuzNumber, SurahNumber) -> Unit,
    onHizbClick: (HizbNumber, SurahNumber) -> Unit,
    onSurahBookmarkClick: (SurahNumber, VerseNumber?) -> Unit,
    onJuzBookmarkClick: (JuzNumber, SurahNumber, VerseNumber?) -> Unit,
    onHizbBookmarkClick: (HizbNumber, SurahNumber, VerseNumber?) -> Unit,
    viewModel: QuranViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (!uiState.isLoading) {
        QuranScreen(
            bottomBar = bottomBar,
            surahs = uiState.surahs,
            allJuz = uiState.allJuz,
            allHizb = uiState.allHizb,
            surahVersePreferences = uiState.preferences!!,
            onSurahClick = onSurahClick,
            onJuzClick = onJuzClick,
            onHizbClick = onHizbClick,
            onSurahBookmarkClick = onSurahBookmarkClick,
            onJuzBookmarkClick = onJuzBookmarkClick,
            onHizbBookmarkClick = onHizbBookmarkClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuranScreen(
    bottomBar: @Composable () -> Unit,
    surahs: List<Surah>,
    allJuz: List<Juz>,
    allHizb: List<Hizb>,
    surahVersePreferences: SurahVersePreferences,
    onSurahClick: (SurahNumber) -> Unit,
    onJuzClick: (JuzNumber, SurahNumber) -> Unit,
    onHizbClick: (HizbNumber, SurahNumber) -> Unit,
    onSurahBookmarkClick: (SurahNumber, VerseNumber?) -> Unit,
    onJuzBookmarkClick: (JuzNumber, SurahNumber, VerseNumber?) -> Unit,
    onHizbBookmarkClick: (HizbNumber, SurahNumber, VerseNumber?) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = QuranTab.SURAH.ordinal,
        pageCount = { QuranTab.entries.size }
    )
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedPage by remember {
        derivedStateOf { pagerState.currentPage }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TitleTopBar(
                title = stringResource(R.string.quran),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRowSection(
                selectedTabIndex = selectedPage,
                currentTab = pagerState.currentPage,
                onTabClick = { scope.launch { pagerState.animateScrollToPage(it) } }
            )

            PagerSection(
                pagerState = pagerState,
                surahs = surahs,
                allJuz = allJuz,
                allHizb = allHizb,
                surahVersePreferences = surahVersePreferences,
                onSurahClick = onSurahClick,
                onJuzClick = onJuzClick,
                onHizbClick = onHizbClick,
                onSurahBookmarkClick = onSurahBookmarkClick,
                onJuzBookmarkClick = onJuzBookmarkClick,
                onHizbBookmarkClick = onHizbBookmarkClick
            )
        }
    }
}

@Composable
private fun TabRowSection(
    selectedTabIndex: Int,
    currentTab: Int,
    onTabClick: (Int) -> Unit
) {
    PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
        QuranTab.entries.forEachIndexed { index, item ->
            Tab(
                selected = currentTab == index,
                onClick = { onTabClick(index) },
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
}

@Composable
private fun PagerSection(
    pagerState: PagerState,
    surahs: List<Surah>,
    allJuz: List<Juz>,
    allHizb: List<Hizb>,
    surahVersePreferences: SurahVersePreferences,
    onSurahClick: (SurahNumber) -> Unit,
    onJuzClick: (JuzNumber, SurahNumber) -> Unit,
    onHizbClick: (HizbNumber, SurahNumber) -> Unit,
    onSurahBookmarkClick: (SurahNumber, VerseNumber?) -> Unit,
    onJuzBookmarkClick: (JuzNumber, SurahNumber, VerseNumber?) -> Unit,
    onHizbBookmarkClick: (HizbNumber, SurahNumber, VerseNumber?) -> Unit
) {
    HorizontalPager(state = pagerState) { page ->
        val tab = QuranTab.entries[page]
        val surahVerseBookmark = when (tab) {
            QuranTab.SURAH -> surahVersePreferences.surahBookmark
            QuranTab.JUZ -> surahVersePreferences.juzBookmark
            QuranTab.HIZB -> surahVersePreferences.hizbBookmark
        }
        val listState = rememberLazyListState()

        Box {
            LazyColumn(state = listState) {
                surahVerseBookmark?.let {
                    item {
                        Column(
                            modifier = Modifier
                                .padding(top = MaterialTheme.padding.medium)
                                .padding(horizontal = MaterialTheme.padding.medium),
                            verticalArrangement = Arrangement.smallSpacing()
                        ) {
                            SectionTitle(text = stringResource(R.string.last_read))

                            SurahVerseBookmarkCard(
                                surahVerse = surahVerseBookmark,
                                quranTab = tab,
                                onClick = {
                                    when (tab) {
                                        QuranTab.SURAH -> onSurahBookmarkClick(
                                            surahVerseBookmark.surah.number,
                                            surahVerseBookmark.verse.verseNumber
                                        )

                                        QuranTab.JUZ -> onJuzBookmarkClick(
                                            surahVerseBookmark.verse.juz,
                                            surahVerseBookmark.surah.number,
                                            surahVerseBookmark.verse.verseNumber
                                        )

                                        QuranTab.HIZB -> onHizbBookmarkClick(
                                            surahVerseBookmark.verse.hizb,
                                            surahVerseBookmark.surah.number,
                                            surahVerseBookmark.verse.verseNumber
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                item {
                    SectionTitle(
                        modifier = Modifier.padding(
                            top = MaterialTheme.padding.medium,
                            start = MaterialTheme.padding.medium,
                            end = MaterialTheme.padding.medium,
                            bottom = MaterialTheme.padding.small
                        ),
                        text = when (tab) {
                            QuranTab.SURAH -> stringResource(R.string.all_surahs)
                            QuranTab.JUZ -> stringResource(R.string.all_juz)
                            QuranTab.HIZB -> stringResource(R.string.all_hizb)
                        },
                    )
                }

                when (tab) {
                    QuranTab.SURAH -> {
                        items(surahs.size) { index ->
                            val surah = surahs[index]
                            QuranListItem(
                                number = surah.number,
                                headline = surah.transliteration,
                                leading = surah.translation,
                                displayDivider = index != surahs.size.asIndex(),
                                onClick = { onSurahClick(surah.number) }
                            )
                        }
                    }

                    QuranTab.JUZ -> {
                        items(allJuz.size) { index ->
                            val juz = allJuz[index]
                            QuranListItem(
                                number = juz.number,
                                headline = "${stringResource(R.string.juz)} ${juz.number}",
                                leading = "${juz.firstSurahVerse.surah.transliteration} - ${
                                    stringResource(
                                        R.string.verse
                                    )
                                } ${juz.firstSurahVerse.verse.verseNumber}",
                                displayDivider = index != allJuz.size.asIndex(),
                                onClick = {
                                    onJuzClick(
                                        juz.number,
                                        juz.firstSurahVerse.surah.number
                                    )
                                }
                            )
                        }
                    }

                    QuranTab.HIZB -> {
                        items(allHizb.size) { index ->
                            val hizb = allHizb[index]
                            QuranListItem(
                                number = hizb.number,
                                headline = "${stringResource(R.string.hizb)} ${hizb.number}",
                                leading = "${hizb.firstSurahVerse.surah.transliteration} - ${
                                    stringResource(
                                        R.string.verse
                                    )
                                } ${hizb.firstSurahVerse.verse.verseNumber}",
                                displayDivider = index != allHizb.size.asIndex(),
                                onClick = {
                                    onHizbClick(
                                        hizb.number,
                                        hizb.firstSurahVerse.surah.number
                                    )
                                }
                            )
                        }
                    }
                }
            }

            VerticalScrollBarIndicator(
                modifier = Modifier.align(Alignment.CenterEnd),
                state = listState,
                itemCount = when (tab) {
                    QuranTab.SURAH -> surahs.size
                    QuranTab.JUZ -> allJuz.size
                    QuranTab.HIZB -> allHizb.size
                }
            )
        }
    }
}

@Composable
private fun SurahVerseBookmarkCard(
    surahVerse: SurahVerse,
    quranTab: QuranTab,
    onClick: () -> Unit
) {
    val title = when (quranTab) {
        QuranTab.SURAH -> surahVerse.surah.transliteration
        QuranTab.JUZ -> "${stringResource(R.string.juz)} ${surahVerse.verse.juz}"
        QuranTab.HIZB -> "${stringResource(R.string.hizb)} ${surahVerse.verse.hizb}"
    }

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        ListItem(
            leadingContent = {
                Icon(
                    painter = painterResource(SurahMetadata.getSurahImageResId(surahVerse.surah.number)),
                    contentDescription = null
                )
            },
            headlineContent = { Text(text = title) },
            supportingContent = { Text(text = "${stringResource(R.string.verse)} ${surahVerse.verse.verseNumber}") },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
                leadingIconColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
private fun QuranListItem(
    number: Int,
    headline: String,
    leading: String,
    displayDivider: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = { Text(headline) },
        leadingContent = { Text(number.toString()) },
        supportingContent = { Text(leading) }
    )

    if (displayDivider) {
        HorizontalDivider()
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
            surahVersePreferences = surahVersePreferencesFixture,
            onSurahClick = {},
            onJuzClick = {_, _, -> },
            onHizbClick = {_, _ -> },
            onSurahBookmarkClick = { _, _ -> },
            onJuzBookmarkClick = { _, _, _ -> },
            onHizbBookmarkClick = { _, _, _ -> }
        )
    }
}

@PhonePreviews
@Composable
private fun SurahVerseBookmarkCardPreview() {
    Previews.Preview {
        Column {
            SurahVerseBookmarkCard(
                surahVerse = surahVerseFixture,
                quranTab = QuranTab.SURAH,
                onClick = {}
            )
        }
    }
}