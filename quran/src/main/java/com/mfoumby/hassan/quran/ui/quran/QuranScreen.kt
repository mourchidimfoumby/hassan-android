package com.mfoumby.hassan.quran.ui.quran

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.Modifier
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
            quranContentType = uiState.contentType,
            onContentTypeChange = viewModel::onQuranContentTypeChange,
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
    quranContentType: QuranViewModel.QuranContentType,
    onContentTypeChange: (QuranViewModel.QuranContentType) -> Unit,
    onSurahClick: (SurahNumber) -> Unit,
    onJuzClick: (JuzNumber, SurahNumber) -> Unit,
    onHizbClick: (HizbNumber, SurahNumber) -> Unit,
    onSurahBookmarkClick: (SurahNumber, VerseNumber?) -> Unit,
    onJuzBookmarkClick: (JuzNumber, SurahNumber, VerseNumber?) -> Unit,
    onHizbBookmarkClick: (HizbNumber, SurahNumber, VerseNumber?) -> Unit,
    onSearchClick: () -> Unit
) {
    var showFilterContentTypeMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TitleTopBar(
                title = stringResource(R.string.quran),
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            painter = painterResource(com.mfoumby.hassan.common.R.drawable.ic_outline_search),
                            contentDescription = stringResource(com.mfoumby.hassan.common.R.string.search)
                        )
                    }

                    IconButton(onClick = { showFilterContentTypeMenu = true }) {
                        Icon(
                            painter = painterResource(com.mfoumby.hassan.common.R.drawable.ic_outline_filter_list),
                            contentDescription = "Filter content type"
                        )
                    }
                    DropdownMenu(
                        expanded = showFilterContentTypeMenu,
                        onDismissRequest = { showFilterContentTypeMenu = false }
                    ) {
                        QuranViewModel.QuranContentType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(stringResource(type.stringRes)) },
                                onClick = {
                                    onContentTypeChange(type)
                                    showFilterContentTypeMenu = false
                                },
                                leadingIcon = if (type == quranContentType) {
                                    {
                                        Icon(
                                            painter = painterResource(com.mfoumby.hassan.common.R.drawable.ic_outline_check),
                                            contentDescription = null
                                        )
                                    }
                                } else null
                            )
                        }
                    }
                }
            )
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        QuranContent(
            modifier = Modifier.padding(innerPadding),
            surahs = surahs,
            allJuz = allJuz,
            allHizb = allHizb,
            surahVersePreferences = surahVersePreferences,
            quranContentType = quranContentType,
            onSurahClick = onSurahClick,
            onJuzClick = onJuzClick,
            onHizbClick = onHizbClick,
            onSurahBookmarkClick = onSurahBookmarkClick,
            onJuzBookmarkClick = onJuzBookmarkClick,
            onHizbBookmarkClick = onHizbBookmarkClick
        )
    }
}

@Composable
private fun QuranContent(
    modifier: Modifier = Modifier,
    surahs: List<Surah>,
    allJuz: List<Juz>,
    allHizb: List<Hizb>,
    surahVersePreferences: SurahVersePreferences,
    quranContentType: QuranViewModel.QuranContentType,
    onSurahClick: (SurahNumber) -> Unit,
    onJuzClick: (JuzNumber, SurahNumber) -> Unit,
    onHizbClick: (HizbNumber, SurahNumber) -> Unit,
    onSurahBookmarkClick: (SurahNumber, VerseNumber?) -> Unit,
    onJuzBookmarkClick: (JuzNumber, SurahNumber, VerseNumber?) -> Unit,
    onHizbBookmarkClick: (HizbNumber, SurahNumber, VerseNumber?) -> Unit
) {
    val itemCount = when (quranContentType) {
        QuranViewModel.QuranContentType.SURAH -> surahs.size
        QuranViewModel.QuranContentType.JUZ -> allJuz.size
        QuranViewModel.QuranContentType.HIZB -> allHizb.size
    }

    SimpleLazyColumn(
        modifier = modifier,
        itemCount = itemCount
    ) {
        surahVersePreferences.surahVerseBookmark?.let {
            item {
                BookmarkSection(
                    modifier = Modifier
                        .padding(top = MaterialTheme.padding.medium)
                        .padding(horizontal = MaterialTheme.padding.medium),
                    quranContentType = quranContentType,
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
                text = when (quranContentType) {
                    QuranViewModel.QuranContentType.SURAH -> stringResource(R.string.all_surahs)
                    QuranViewModel.QuranContentType.JUZ -> stringResource(R.string.all_juz)
                    QuranViewModel.QuranContentType.HIZB -> stringResource(R.string.all_hizb)
                },
            )
        }

        when (quranContentType) {
            QuranViewModel.QuranContentType.SURAH -> {
                itemsIndexed(surahs) { index, surah ->
                    SurahListItem(
                        surah = surah,
                        modifier = Modifier.clickable(onClick = { onSurahClick(surah.number) }),
                    )

                    if (index != surahs.size.asIndex()) {
                        HorizontalDivider()
                    }
                }
            }

            QuranViewModel.QuranContentType.JUZ -> {
                itemsIndexed(allJuz) { index, juz ->
                    JuzListItem(
                        surahVerse = juz.firstSurahVerse,
                        modifier = Modifier.clickable(onClick = { onJuzClick(juz.number, juz.firstSurahVerse.surah.number) })
                    )

                    if (index != allJuz.size.asIndex()) {
                        HorizontalDivider()
                    }
                }
            }

            QuranViewModel.QuranContentType.HIZB -> {
                itemsIndexed(allHizb) { index, hizb ->
                    HizbListItem(
                        surahVerse = hizb.firstSurahVerse,
                        modifier = Modifier.clickable(onClick = { onHizbClick(hizb.number, hizb.firstSurahVerse.surah.number) }),
                    )

                    if (index != allHizb.size.asIndex()) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun BookmarkSection(
    modifier: Modifier = Modifier,
    quranContentType: QuranViewModel.QuranContentType,
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

        when (quranContentType) {
            QuranViewModel.QuranContentType.SURAH -> SurahBookmarkCard(
                surahVerse = surahVerseBookmark,
                onClick = {
                    onSurahBookmarkClick(
                        surahVerseBookmark.surah.number,
                        surahVerseBookmark.verse.verseNumber
                    )
                }
            )

            QuranViewModel.QuranContentType.JUZ -> JuzBookmarkCard(
                surahVerse = surahVerseBookmark,
                onClick = {
                    onJuzBookmarkClick(
                        surahVerseBookmark.verse.juzNumber,
                        surahVerseBookmark.surah.number,
                        surahVerseBookmark.verse.verseNumber
                    )
                }
            )

            QuranViewModel.QuranContentType.HIZB -> HizbBookmarkCard(
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
            quranContentType = QuranViewModel.QuranContentType.SURAH,
            onContentTypeChange = {},
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