package com.mfoumby.hassan.quran.ui.surahverse

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.mfoumby.hassan.common.SingleUiEvent
import com.mfoumby.hassan.common.domain.extension.asIndex
import com.mfoumby.hassan.common.domain.extension.fromIndex
import com.mfoumby.hassan.common.domain.extension.half
import com.mfoumby.hassan.common.snackbarLauncher
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.BackTopBar
import com.mfoumby.hassan.quran.QuranMode
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.Constants
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import com.mfoumby.hassan.quran.domain.entity.SurahVersePlayerManifest
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation
import com.mfoumby.hassan.quran.domain.surahFixture
import com.mfoumby.hassan.quran.domain.surahVerseFixtures
import com.mfoumby.hassan.quran.domain.surahVerseFixtures3
import com.mfoumby.hassan.quran.domain.surahVersePlayerManifestFixture
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
import kotlin.math.max

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
    val snackBarHostState = remember { SnackbarHostState() }
    val showSnackbar = snackbarLauncher(snackBarHostState)
    val surahVersePlayerData = uiState.playerManifest
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
            uiState.audioDownloadProgress?.let { audioDownloadProgress ->
                DownloadingAudioDialog(
                    progress = audioDownloadProgress.progress.progress,
                    surah = audioDownloadProgress.surah,
                    reciter = audioDownloadProgress.reciter,
                    currentStep = audioDownloadProgress.currentStep,
                    totalSteps = audioDownloadProgress.totalSteps,
                    onCancelDownloadingClick = {
                        viewModel.stopAudioDownload()
                        activeDialog = null
                    }
                )
            }
        }

        else -> Unit
    }

    LaunchedEffect(surahVersePlayerData?.surahVerseAudios) {
        surahVersePlayerData?.surahVerseAudios?.values?.map {
            MediaItem.Builder()
                .setUri(it.audioUri)
                .setMediaId(it.id.toString())
                .build()
        }?.also {
            player.setMediaItems(it)
            player.prepare()
        }
    }

    LaunchedEffect(surahVersePlayerData?.state) {
        when (val state = surahVersePlayerData?.state) {
            is SurahVersePlayerManifest.State.Playing -> {
                val index = max(surahVersePlayerData.surahVerseAudios.values.indexOf(state.surahVerseAudio), 0)
                player.seekTo(index, 0)
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
                    mediaItem?.mediaId?.toInt()?.let { id ->
                        val surahNumber = SurahVerseAudio.getSurahNumberFromId(id)
                        val verseNumber = SurahVerseAudio.getVerseNumberFromId(id)
                        viewModel.onAudioChange(surahNumber, verseNumber)
                    }
                }
            }
        }

        player.addListener(listener)

        onDispose {
            player.release()
            player.removeListener(listener)
        }
    }

    if (!uiState.isLoading) {
        SurahVerseScreen(
            surah = uiState.surah!!,
            surahVerses = uiState.surahVerses,
            surahVerseTranslations = uiState.translations,
            juz = uiState.juz!!,
            hizb = uiState.hizb!!,
            page = uiState.page!!,
            surahVersePreferences = uiState.preferences!!,
            informativeDisplayMode = uiState.informativeDisplayMode!!,
            quranMode = quranMode,
            surahVersePlayerManifest = surahVersePlayerData,
            player = player,
            currentTrack = uiState.currentAudioTrack,
            snackBarHostState = snackBarHostState,
            onBackClick = onBackClick,
            onDisplayModeClick = viewModel::onDisplayModeChange,
            onDisplayTajweedChange = viewModel::onDisplayTajweedChange,
            onTranslationLanguageClick = onTranslationLanguageClick,
            onDisplayTranslationChange = viewModel::onDisplayTranslationChange,
            onReciterClick = onReciterClick,
            onAutomaticScrollingChange = viewModel::onAutomaticScrollingChange,
            onPlaySurahVerseAudioClick = viewModel::onPlaySurahVerseAudio,
            onPageChange = viewModel::onPageChange,
            onSaveBookmark = viewModel::onSaveBookmark
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
    hizb: Int,
    page: Int,
    surahVersePreferences: SurahVersePreferences,
    informativeDisplayMode: SurahVerseViewModel.InformativeDisplayMode,
    surahVersePlayerManifest: SurahVersePlayerManifest?,
    player: Player?,
    currentTrack: SurahVerseAudio?,
    quranMode: QuranMode,
    snackBarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onDisplayTajweedChange: (Boolean) -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onDisplayTranslationChange: (Boolean) -> Unit,
    onReciterClick: () -> Unit,
    onAutomaticScrollingChange: (Boolean) -> Unit,
    onPlaySurahVerseAudioClick: (SurahVerse) -> Unit,
    onDisplayModeClick: (SurahVerseViewModel.InformativeDisplayMode) -> Unit,
    onPageChange: (Int) -> Unit,
    onSaveBookmark: (SurahVerse) -> Unit
) {
    var activeBottomSheet by remember { mutableStateOf<SurahVerseBottomSheet?>(null) }
    val title = when (quranMode) {
        is QuranMode.SurahMode -> surah.transliteration
        is QuranMode.JuzMode -> "${stringResource(R.string.juz)} $juz"
        is QuranMode.HizbMode -> "${stringResource(R.string.hizb)} $hizb"
    }
    var scrollValue by remember { mutableStateOf(ScrollValue()) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val bookmarkState by rememberUpdatedState(
        BookmarkState(
            surahVerses = surahVerses,
            scrollValue = scrollValue,
            displayMode = surahVersePreferences.displayMode
        )
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    onSaveBookmark(retrieveLastSurahVerseRead(bookmarkState.surahVerses, bookmarkState.scrollValue, bookmarkState.displayMode))
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
            Box(modifier = Modifier.weight(1f)) {
                when (informativeDisplayMode) {
                    is SurahVerseViewModel.InformativeDisplayMode.ListMode -> {
                        SurahVerseListMode(
                            surah = surah,
                            surahVerses = surahVerses,
                            juz = juz,
                            hizb = hizb,
                            surahVerseTranslations = surahVerseTranslations,
                            surahVersePreferences = surahVersePreferences,
                            quranMode = quranMode,
                            surahVerseToScroll = informativeDisplayMode.surahVerse,
                            currentTrack = currentTrack,
                            onPageChange = onPageChange,
                            onSurahVerseClick = {
                                activeBottomSheet = SurahVerseBottomSheet.VerseBottomSheet(it)
                            },
                            onScrollValueChange = { scrollValue = it }
                        )
                    }

                    is SurahVerseViewModel.InformativeDisplayMode.PageMode -> {
                        SurahVersePageMode(
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

            when (val state = surahVersePlayerManifest?.state) {
                is SurahVersePlayerManifest.State.Playing -> {
                    SurahVersePlayer(
                        player = player,
                        reciter = surahVersePlayerManifest.reciter,
                        surahVerseAudio = state.surahVerseAudio
                    )
                }

                else -> Unit
            }
        }
    }

    when (val bottomSheet = activeBottomSheet) {
        SurahVerseBottomSheet.SettingsBottomSheet -> {
            SurahVerseSettingsBottomSheet(
                onDismissRequest = { activeBottomSheet = null },
                displayMode = surahVersePreferences.displayMode,
                displayTajweed = surahVersePreferences.displayTajweed,
                translationLanguage = surahVersePreferences.translationLanguage,
                displayTranslation = surahVersePreferences.displayTranslation,
                reciter = surahVersePreferences.reciter,
                audioAutomaticScrolling = surahVersePreferences.audioAutomaticScrolling,
                onDisplayModeClick = { displayMode ->
                    if (surahVersePreferences.displayMode != displayMode) {
                        val surahVerse = retrieveLastSurahVerseRead(surahVerses, scrollValue, surahVersePreferences.displayMode)
                        when (displayMode) {
                            SurahVersePreferences.DisplayMode.LIST -> SurahVerseViewModel.InformativeDisplayMode.ListMode(surahVerse)
                            SurahVersePreferences.DisplayMode.PAGE -> SurahVerseViewModel.InformativeDisplayMode.PageMode(surahVerse)
                        }.let(onDisplayModeClick)
                    }
                },
                onTranslationLanguageClick = {
                    activeBottomSheet = null
                    onTranslationLanguageClick()
                },
                onDisplayTranslationChange = onDisplayTranslationChange,
                onReciterClick = {
                    activeBottomSheet = null
                    onReciterClick()
                },
                onAutomaticScrollingChange = onAutomaticScrollingChange,
                onDisplayTajweedChange = onDisplayTajweedChange
            )
        }

        is SurahVerseBottomSheet.VerseBottomSheet -> {
            SurahVerseBottomSheet(
                onDismissRequest = { activeBottomSheet = null },
                onPlaySurahVerseAudioClick = {
                    activeBottomSheet = null
                    onPlaySurahVerseAudioClick(bottomSheet.surahVerse)
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
    hizb: Int,
    surahVerseTranslations: List<SurahVerseTranslation>,
    surahVersePreferences: SurahVersePreferences,
    quranMode: QuranMode,
    surahVerseToScroll: SurahVerse?,
    currentTrack: SurahVerseAudio?,
    onPageChange: (Int) -> Unit,
    onSurahVerseClick: (SurahVerse) -> Unit,
    onScrollValueChange: (ScrollValue) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = when (quranMode) {
            is QuranMode.SurahMode -> surah.number
            is QuranMode.JuzMode -> juz
            is QuranMode.HizbMode -> hizb
        }.asIndex(),
        pageCount = {
            when (quranMode) {
                is QuranMode.SurahMode -> Constants.TOTAL_QURAN_SURAH
                is QuranMode.JuzMode -> Constants.TOTAL_QURAN_JUZ
                is QuranMode.HizbMode -> Constants.TOTAL_QURAN_HIZB
            }
        }
    )

    LaunchedEffect(Unit) {
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
                is QuranMode.HizbMode -> surahVerses.filter { it.verse.hizb == page.fromIndex() }
            },
            surahVerseTranslations = surahVerseTranslations,
            surahVersePreferences = surahVersePreferences,
            surahVerseToScroll = surahVerseToScroll,
            audioAutomaticScrolling = surahVersePreferences.audioAutomaticScrolling,
            currentAudioTrack = currentTrack,
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
        initialPage = page.asIndex(),
        pageCount = { Constants.TOTAL_QURAN_PAGES }
    )

    LaunchedEffect(Unit) {
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

data class BookmarkState(
    val surahVerses: List<SurahVerse>,
    val scrollValue: ScrollValue,
    val displayMode: SurahVersePreferences.DisplayMode
)

private fun retrieveLastSurahVerseRead(
    surahVerses: List<SurahVerse>,
    scrollValue: ScrollValue,
    displayMode: SurahVersePreferences.DisplayMode
): SurahVerse {
    return when (displayMode) {
        SurahVersePreferences.DisplayMode.LIST -> surahVerses[scrollValue.currentValue]

        SurahVersePreferences.DisplayMode.PAGE -> {
            val index = if (scrollValue.currentValue > scrollValue.maxValue.half()) {
                surahVerses.size.half()
            } else 0
            surahVerses[index]
        }
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
            surah = surahVerseFixtures3.first().surah,
            surahVerses = surahVerseFixtures3,
            surahVerseTranslations = surahVerseTranslationFixtures,
            juz = surahVerseFixtures3.first().verse.juz,
            hizb = surahVerseFixtures3.first().verse.hizb,
            page = surahVerseFixtures3.first().verse.page,
            surahVersePreferences = surahVersePreferencesFixture,
            informativeDisplayMode = SurahVerseViewModel.InformativeDisplayMode.ListMode(surahVerseFixtures.first()),
            quranMode = QuranMode.SurahMode(surahFixture.number, null),
            surahVersePlayerManifest = surahVersePlayerManifestFixture,
            player = null,
            currentTrack = null,
            snackBarHostState = SnackbarHostState(),
            onBackClick = {},
            onDisplayModeClick = {},
            onDisplayTajweedChange = {},
            onTranslationLanguageClick = {},
            onDisplayTranslationChange = {},
            onReciterClick = {},
            onAutomaticScrollingChange = {},
            onPlaySurahVerseAudioClick = {},
            onPageChange = {},
            onSaveBookmark = {}
        )
    }
}