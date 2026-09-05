package com.mfoumby.hassan.quran.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mfoumby.hassan.common.domain.extension.asIndex
import com.mfoumby.hassan.common.extension.smallSpacing
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.SectionTitle
import com.mfoumby.hassan.common.ui.components.SimpleLazyColumn
import com.mfoumby.hassan.common.ui.components.TitleTopBar
import com.mfoumby.hassan.common.ui.theme.padding
import com.mfoumby.hassan.common.ui.theme.transparentListItemColor
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
    onSearchClick: () -> Unit,
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
            onHizbBookmarkClick = onHizbBookmarkClick,
            onSearchClick = onSearchClick
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
    onHizbBookmarkClick: (HizbNumber, SurahNumber, VerseNumber?) -> Unit,
    onSearchClick: () -> Unit
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
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            painter = painterResource(com.mfoumby.hassan.common.R.drawable.ic_outline_search),
                            contentDescription = stringResource(com.mfoumby.hassan.common.R.string.search)
                        )
                    }
                }
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
        val quranTab = QuranTab.entries[page]
        val surahVerseBookmark = when (quranTab) {
            QuranTab.SURAH -> surahVersePreferences.surahBookmark
            QuranTab.JUZ -> surahVersePreferences.juzBookmark
            QuranTab.HIZB -> surahVersePreferences.hizbBookmark
        }
        val itemCount = when (quranTab) {
            QuranTab.SURAH -> surahs.size
            QuranTab.JUZ -> allJuz.size
            QuranTab.HIZB -> allHizb.size
        }

        SimpleLazyColumn(itemCount = itemCount) {
            surahVerseBookmark?.let {
                item {
                    BookmarkSection(
                        modifier = Modifier
                            .padding(top = MaterialTheme.padding.medium)
                            .padding(horizontal = MaterialTheme.padding.medium),
                        quranTab = quranTab,
                        surahVerseBookmark = it,
                        onSurahBookmarkClick = onSurahBookmarkClick,
                        onJuzBookmarkClick = onJuzBookmarkClick,
                        onHizbBookmarkClick = onHizbBookmarkClick
                    )
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
                    text = when (quranTab) {
                        QuranTab.SURAH -> stringResource(R.string.all_surahs)
                        QuranTab.JUZ -> stringResource(R.string.all_juz)
                        QuranTab.HIZB -> stringResource(R.string.all_hizb)
                    },
                )
            }

            when (quranTab) {
                QuranTab.SURAH -> {
                    quranListItem(surahs) { surah ->
                        SurahListItem(
                            surah = surah,
                            modifier = Modifier.clickable(onClick = { onSurahClick(surah.number) }),
                        )
                    }
                }

                QuranTab.JUZ -> {
                    quranListItem(allJuz) { juz ->
                        JuzListItem(
                            surahVerse = juz.firstSurahVerse,
                            modifier = Modifier.clickable(onClick = { onJuzClick(juz.number, juz.firstSurahVerse.surah.number) })
                        )
                    }
                }

                QuranTab.HIZB -> {
                    quranListItem(allHizb) { hizb ->
                        HizbListItem(
                            surahVerse = hizb.firstSurahVerse,
                            modifier = Modifier.clickable(onClick = { onHizbClick(hizb.number, hizb.firstSurahVerse.surah.number) }),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookmarkSection(
    modifier: Modifier = Modifier,
    quranTab: QuranTab,
    surahVerseBookmark: SurahVerse,
    onSurahBookmarkClick: (SurahNumber, VerseNumber?) -> Unit,
    onJuzBookmarkClick: (JuzNumber, SurahNumber, VerseNumber?) -> Unit,
    onHizbBookmarkClick: (HizbNumber, SurahNumber, VerseNumber?) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        SectionTitle(text = stringResource(R.string.last_read))

        when (quranTab) {
            QuranTab.SURAH -> SurahBookmarkCard(
                surahVerse = surahVerseBookmark,
                onClick = {
                    onSurahBookmarkClick(
                        surahVerseBookmark.surah.number,
                        surahVerseBookmark.verse.verseNumber
                    )
                }
            )

            QuranTab.JUZ -> JuzBookmarkCard(
                surahVerse = surahVerseBookmark,
                onClick = {
                    onJuzBookmarkClick(
                        surahVerseBookmark.verse.juzNumber,
                        surahVerseBookmark.surah.number,
                        surahVerseBookmark.verse.verseNumber
                    )
                }
            )

            QuranTab.HIZB -> HizbBookmarkCard(
                surahVerse = surahVerseBookmark,
                onClick = {
                    onHizbBookmarkClick(
                        surahVerseBookmark.verse.hizbNumber,
                        surahVerseBookmark.surah.number,
                        surahVerseBookmark.verse.verseNumber
                    )
                }
            )
        }
    }
}

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
private fun JuzListItem(
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
private fun HizbListItem(
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
private fun SurahBookmarkCard(
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
private fun JuzBookmarkCard(
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
private fun HizbBookmarkCard(
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

private inline fun <T> LazyListScope.quranListItem(
    items: List<T>,
    crossinline itemContent: @Composable (LazyItemScope.(T) -> Unit)
) {
    itemsIndexed(items) { index, item ->
        itemContent(item)

        if (index != items.size.asIndex()) {
            HorizontalDivider()
        }
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
            onHizbBookmarkClick = { _, _, _ -> },
            onSearchClick = {}
        )
    }
}

@PhonePreviews
@Composable
private fun SurahBookmarkCardPreview() {
    Previews.Preview {
        Column {
            SurahBookmarkCard(
                surahVerse = surahVerseFixture,
                onClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun JuzBookmarkCardPreview() {
    Previews.Preview {
        Column {
            JuzBookmarkCard(
                surahVerse = surahVerseFixture,
                onClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun HizbBookmarkCardPreview() {
    Previews.Preview {
        Column {
            HizbBookmarkCard(
                surahVerse = surahVerseFixture,
                onClick = {}
            )
        }
    }
}