package com.mfoumby.hassan.quran.ui.surahverse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.mfoumby.hassan.common.SingleUiEvent
import com.mfoumby.hassan.common.domain.extension.fromIndex
import com.mfoumby.hassan.common.domain.extension.half
import com.mfoumby.hassan.common.domain.extension.toIndex
import com.mfoumby.hassan.common.snackbarLauncher
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.BackTopBar
import com.mfoumby.hassan.quran.QuranMode
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.Constants
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVersePlayerData
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation
import com.mfoumby.hassan.quran.domain.surahFixture
import com.mfoumby.hassan.quran.domain.surahVerseFixtures
import com.mfoumby.hassan.quran.domain.surahVersePreferencesFixture
import com.mfoumby.hassan.quran.domain.surahVerseTranslationFixtures
import com.mfoumby.hassan.quran.ui.surahverse.components.DownloadAudioDialog
import com.mfoumby.hassan.quran.ui.surahverse.components.DownloadingAudioDialog
import com.mfoumby.hassan.quran.ui.surahverse.components.SurahVerseList
import com.mfoumby.hassan.quran.ui.surahverse.components.SurahVersePage
import com.mfoumby.hassan.quran.ui.surahverse.components.SurahVersePlayer
import com.mfoumby.hassan.quran.ui.surahverse.components.bottomsheets.SurahVerseBottomSheet
import com.mfoumby.hassan.quran.ui.surahverse.components.bottomsheets.SurahVerseSettingsBottomSheet
import kotlinx.coroutines.flow.drop
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahVerseDestination(
    quranMode: QuranMode,
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onReciterClick: () -> Unit,
    viewModel: SurahVerseViewModel = koinViewModel(
        parameters = { parametersOf(quranMode) }
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val resources = LocalResources.current
    val player = remember { ExoPlayer.Builder(context).build() }
    val surahVerseAudios = uiState.surahVersePlayerData?.surahVerseAudios
    val surahVersePlayerData = uiState.surahVersePlayerData
    val snackBarHostState = remember { SnackbarHostState() }
    val showSnackbar = snackbarLauncher(snackBarHostState)
    var activeDialog by remember { mutableStateOf<SurahVerseDialog?>(null) }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                SurahVerseViewModel.SurahVerseUiEvent.AudioDownloadSuccess -> activeDialog = null

                is SurahVerseViewModel.SurahVerseUiEvent.AudioDownloadError -> {
                    activeDialog = null
                    showSnackbar(resources.getString(it.messageId))
                }

                SurahVerseViewModel.SurahVerseUiEvent.DownloadAudioRequest ->
                    activeDialog = SurahVerseDialog.DownloadAudioDialog

                is SingleUiEvent.Error -> {
                    activeDialog = null
                    showSnackbar(resources.getString(it.messageId))
                }
            }
        }
    }

    when (activeDialog) {
        SurahVerseDialog.DownloadAudioDialog -> {
            DownloadAudioDialog(
                onConfirm = {
                    viewModel.downloadAudio()
                    activeDialog = SurahVerseDialog.DownloadingAudioDialog
                },
                onCancel = { activeDialog = null }
            )
        }

        SurahVerseDialog.DownloadingAudioDialog -> {
            var progress by remember { mutableFloatStateOf(0f) }
            uiState.audioDownloadProgress?.progress?.let { progress = it }
            DownloadingAudioDialog(
                progress = progress,
                surah = uiState.surah!!,
                reciter = uiState.surahVersePreferences?.reciter!!,
                onCancelDownloadingClick = {
                    viewModel.stopAudioDownload()
                    activeDialog = null
                }
            )
        }

        else -> Unit
    }

    LaunchedEffect(surahVerseAudios) {
        surahVerseAudios?.map {
            MediaItem.Builder()
                .setUri(it.audioUri)
                .setMediaId(it.verseNumber.toString())
                .build()
        }?.also {
            player.setMediaItems(it)
            player.prepare()
        }
    }

    LaunchedEffect(surahVersePlayerData?.state) {
        when (val state = surahVersePlayerData?.state) {
            is SurahVersePlayerData.State.Playing -> {
                player.seekTo(state.surahVerseAudio.verseNumber - 1, 0)
                player.play()
            }

            else -> Unit
        }
    }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onMediaItemTransition(
                mediaItem: MediaItem?,
                reason: Int
            ) {
                if (
                    reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO ||
                    reason == Player.MEDIA_ITEM_TRANSITION_REASON_SEEK
                ) {
                    mediaItem?.mediaId?.toInt()?.let(viewModel::onAudioChange)
                }
            }
        }

        player.addListener(listener)

        onDispose {
            player.release()
            player.removeListener(listener)
        }
    }

    if (!uiState.initializing) {
        SurahVerseScreen(
            surah = uiState.surah!!,
            surahVerses = uiState.surahVerses,
            surahVerseTranslations = uiState.surahVerseTranslations,
            juz = uiState.juz,
            page = uiState.page,
            surahVersePreferences = uiState.surahVersePreferences!!,
            informativeDisplayMode = uiState.informativeDisplayMode!!,
            quranMode = quranMode,
            surahVersePlayerData = surahVersePlayerData,
            player = player,
            snackBarHostState = snackBarHostState,
            onBackClick = onBackClick,
            onTranslationLanguageClick = onTranslationLanguageClick,
            onDisplayTranslationChange = viewModel::onDisplayTranslationChange,
            onReciterClick = onReciterClick,
            onPlayVerseAudioClick = viewModel::onPlayAudio,
            onDisplayModeClick = viewModel::onDisplayModeChange,
            onPageChange = viewModel::onPageChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SurahVerseScreen(
    surah: Surah,
    surahVerses: List<SurahVerse>,
    surahVerseTranslations: List<SurahVerseTranslation>,
    juz: Int,
    page: Int,
    surahVersePreferences: SurahVersePreferences,
    informativeDisplayMode: SurahVerseViewModel.InformativeDisplayMode,
    surahVersePlayerData: SurahVersePlayerData?,
    player: Player?,
    quranMode: QuranMode,
    snackBarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onDisplayTranslationChange: (Boolean) -> Unit,
    onReciterClick: () -> Unit,
    onPlayVerseAudioClick: (Int) -> Unit,
    onDisplayModeClick: (SurahVerseViewModel.InformativeDisplayMode) -> Unit,
    onPageChange: (Int) -> Unit
) {
    var activeBottomSheet by remember { mutableStateOf<SurahVerseBottomSheet?>(null) }
    val title = when (quranMode) {
        is QuranMode.JuzMode -> "${stringResource(R.string.juz)} $juz"
        is QuranMode.SurahMode -> surah.transliteration
    }
    var scrollValue by remember { mutableStateOf(ScrollValue()) }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = title,
                actions = {
                    IconButton(onClick = { activeBottomSheet = SurahVerseBottomSheet.SettingsBottomSheet }) {
                        Icon(
                            painter = painterResource(com.mfoumby.hassan.common.R.drawable.ic_outline_settings),
                            contentDescription = "Show settings"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState) {
                Snackbar(it)
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (informativeDisplayMode) {
                is SurahVerseViewModel.InformativeDisplayMode.ListMode -> {
                    SurahVerseListMode(
                        modifier = Modifier.weight(1f),
                        surah = surah,
                        surahVerses = surahVerses,
                        juz = juz,
                        surahVerseTranslations = surahVerseTranslations,
                        surahVersePreferences = surahVersePreferences,
                        quranMode = quranMode,
                        surahVerseToScroll = informativeDisplayMode.surahVerse,
                        onPageChange = onPageChange,
                        onSurahVerseClick = {
                            activeBottomSheet = SurahVerseBottomSheet.VerseBottomSheet(it)
                        },
                        onScrollValueChange = { scrollValue = it }
                    )
                }

                is SurahVerseViewModel.InformativeDisplayMode.PageMode -> {
                    SurahVersePageMode(
                        modifier = Modifier.weight(1f),
                        surahVerses = surahVerses,
                        page = page,
                        surahVerseToScroll = informativeDisplayMode.surahVerse,
                        onPageChange = onPageChange,
                        onSurahVerseClick = {
                            activeBottomSheet = SurahVerseBottomSheet.VerseBottomSheet(it)
                        },
                        onScrollValueChange = { scrollValue = it }
                    )
                }
            }
        }

        when (val state = surahVersePlayerData?.state) {
            is SurahVersePlayerData.State.Playing -> {
                SurahVersePlayer(
                    player = player,
                    surah = surah,
                    reciter = surahVersePlayerData.reciter,
                    surahVerseAudio = state.surahVerseAudio
                )
            }

            else -> Unit
        }
    }

    when (val bottomSheet = activeBottomSheet) {
        SurahVerseBottomSheet.SettingsBottomSheet -> {
            SurahVerseSettingsBottomSheet(
                onDismissRequest = { activeBottomSheet = null },
                translationLanguage = surahVersePreferences.translationLanguage,
                displayTranslation = surahVersePreferences.displayTranslation,
                reciter = surahVersePreferences.reciter,
                displayMode = surahVersePreferences.displayMode,
                onTranslationLanguageClick = {
                    activeBottomSheet = null
                    onTranslationLanguageClick()
                },
                onDisplayTranslationChange = onDisplayTranslationChange,
                onReciterClick = {
                    activeBottomSheet = null
                    onReciterClick()
                },
                onDisplayModeClick = { displayMode ->
                    if (surahVersePreferences.displayMode != displayMode) {
                        when (displayMode) {
                            SurahVersePreferences.DisplayMode.LIST -> {
                                val index = if (scrollValue.currentValue > scrollValue.maxValue.half()) {
                                    surahVerses.size.half()
                                } else 0
                                val surahVerse = surahVerses[index]
                                SurahVerseViewModel.InformativeDisplayMode.ListMode(surahVerse)
                            }

                            SurahVersePreferences.DisplayMode.PAGE -> {
                                val surahVerse = surahVerses[scrollValue.currentValue]
                                SurahVerseViewModel.InformativeDisplayMode.PageMode(surahVerse)
                            }
                        }.let(onDisplayModeClick)
                    }
                }
            )
        }

        is SurahVerseBottomSheet.VerseBottomSheet -> {
            SurahVerseBottomSheet(
                onDismissRequest = { activeBottomSheet = null },
                onPlayVerseAudioClick = {
                    activeBottomSheet = null
                    onPlayVerseAudioClick(bottomSheet.surahVerse.verse.verseNumber)
                }
            )
        }

        else -> Unit
    }
}

@Composable
private fun SurahVerseListMode(
    modifier: Modifier = Modifier,
    surah: Surah,
    surahVerses: List<SurahVerse>,
    juz: Int,
    surahVerseTranslations: List<SurahVerseTranslation>,
    surahVersePreferences: SurahVersePreferences,
    quranMode: QuranMode,
    surahVerseToScroll: SurahVerse?,
    onPageChange: (Int) -> Unit,
    onSurahVerseClick: (SurahVerse) -> Unit,
    onScrollValueChange: (ScrollValue) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = when (quranMode) {
            is QuranMode.SurahMode -> surah.number
            is QuranMode.JuzMode -> juz
        }.toIndex(),
        pageCount = {
            when (quranMode) {
                is QuranMode.SurahMode -> Constants.TOTAL_QURAN_SURAH
                is QuranMode.JuzMode -> Constants.TOTAL_QURAN_JUZ
            }
        }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .drop(1)
            .collect { page ->
                onPageChange(page.fromIndex())
            }
    }

    HorizontalPager(
        modifier = modifier,
        reverseLayout = true,
        state = pagerState
    ) { page ->
        SurahVerseList(
            modifier = Modifier.fillMaxSize(),
            surahVerses = when (quranMode) {
                is QuranMode.SurahMode -> surahVerses.filter { it.surah.number == page.fromIndex() }
                is QuranMode.JuzMode -> surahVerses.filter { it.verse.juz == page.fromIndex() }
            },
            surahVerseTranslations = surahVerseTranslations,
            surahVersePreferences = surahVersePreferences,
            surahVerseToScroll = surahVerseToScroll,
            onSurahVerseClick = onSurahVerseClick,
            onScrollValueChange = onScrollValueChange
        )
    }
}

@Composable
private fun SurahVersePageMode(
    modifier: Modifier = Modifier,
    surahVerses: List<SurahVerse>,
    page: Int,
    surahVerseToScroll: SurahVerse?,
    onPageChange: (Int) -> Unit,
    onSurahVerseClick: (SurahVerse) -> Unit,
    onScrollValueChange: (ScrollValue) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = page.toIndex(),
        pageCount = { Constants.TOTAL_QURAN_PAGES }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .drop(1)
            .collect { page ->
                onPageChange(page.fromIndex())
            }
    }

    HorizontalPager(
        modifier = modifier,
        reverseLayout = true,
        state = pagerState
    ) { page ->
        SurahVersePage(
            modifier = Modifier.fillMaxSize(),
            surahVerses = surahVerses.filter { it.verse.page == page.fromIndex() },
            surahVerseToScroll = surahVerseToScroll,
            onSurahVerseClick = onSurahVerseClick,
            onScrollValueChange = onScrollValueChange
        )
    }
}

private sealed class SurahVerseBottomSheet {
    data object SettingsBottomSheet: SurahVerseBottomSheet()
    data class VerseBottomSheet(val surahVerse: SurahVerse): SurahVerseBottomSheet()
}

private sealed class SurahVerseDialog {
    data object DownloadAudioDialog: SurahVerseDialog()
    data object DownloadingAudioDialog: SurahVerseDialog()
}

data class ScrollValue(
    val currentValue: Int = 0,
    val maxValue: Int = 0
)

@PhonePreviews
@Composable
private fun SurahVerseScreenPreview() {
    Previews.Preview {
        SurahVerseScreen(
            surah = surahFixture,
            surahVerses = surahVerseFixtures,
            surahVerseTranslations = surahVerseTranslationFixtures,
            juz = surahVerseFixtures.first().verse.juz,
            page = surahVerseFixtures.first().verse.page,
            surahVersePreferences = surahVersePreferencesFixture,
            informativeDisplayMode = SurahVerseViewModel.InformativeDisplayMode.ListMode(surahVerseFixtures.first()),
            quranMode = QuranMode.SurahMode(surahFixture.number),
            surahVersePlayerData = null,
            player = null,
            snackBarHostState = SnackbarHostState(),
            onBackClick = {},
            onTranslationLanguageClick = {},
            onDisplayTranslationChange = {},
            onReciterClick = {},
            onPlayVerseAudioClick = {},
            onDisplayModeClick = {},
            onPageChange = {}
        )
    }
}