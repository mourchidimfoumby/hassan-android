package com.mfoumby.hassan.quran.ui.surahverse

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.mfoumby.hassan.common.R
import com.mfoumby.hassan.common.SingleUiEvent
import com.mfoumby.hassan.common.domain.extension.fromIndex
import com.mfoumby.hassan.common.domain.extension.half
import com.mfoumby.hassan.common.domain.extension.toIndex
import com.mfoumby.hassan.common.snackbarLauncher
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.BackTopBar
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
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahVerseDestination(
    surahNumber: Int,
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onReciterClick: () -> Unit,
    viewModel: SurahVerseViewModel = koinViewModel(
        parameters = { parametersOf(surahNumber) }
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
            page = uiState.page,
            surahVersePreferences = uiState.surahVersePreferences!!,
            informativeDisplayMode = uiState.informativeDisplayMode!!,
            surahVersePlayerData = surahVersePlayerData,
            player = player,
            snackBarHostState = snackBarHostState,
            onBackClick = onBackClick,
            onTranslationLanguageClick = onTranslationLanguageClick,
            onDisplayTranslationChange = viewModel::onDisplayTranslationChange,
            onReciterClick = onReciterClick,
            onPlayVerseAudioClick = viewModel::onPlayAudio,
            onDisplayModeClick = viewModel::onDisplayModeChange,
            onPageChange = viewModel::onPageChange,
            onSurahChange = viewModel::onSurahChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SurahVerseScreen(
    surah: Surah,
    surahVerses: List<SurahVerse>,
    surahVerseTranslations: List<SurahVerseTranslation>,
    page: Int,
    surahVersePreferences: SurahVersePreferences,
    surahVersePlayerData: SurahVersePlayerData?,
    informativeDisplayMode: SurahVerseViewModel.InformativeDisplayMode,
    player: Player?,
    snackBarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onDisplayTranslationChange: (Boolean) -> Unit,
    onReciterClick: () -> Unit,
    onPlayVerseAudioClick: (Int) -> Unit,
    onDisplayModeClick: (SurahVerseViewModel.InformativeDisplayMode) -> Unit,
    onPageChange: (Int) -> Unit,
    onSurahChange: (Int) -> Unit
) {
    var activeBottomSheet by remember { mutableStateOf<SurahVerseBottomSheet?>(null) }
    val listState = rememberLazyListState()
    val pageScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = surah.transliteration,
                actions = {
                    IconButton(onClick = { activeBottomSheet = SurahVerseBottomSheet.SettingsBottomSheet }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_outline_settings),
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
                    LaunchedEffect(Unit) {
                        val index = max(surahVerses.indexOf(informativeDisplayMode.surahVerse), 0)
                        listState.animateScrollToItem(index)
                    }

                    SurahVerseListMode(
                        modifier = Modifier.weight(1f),
                        surah = surah,
                        surahVerses = surahVerses,
                        surahVerseTranslations = surahVerseTranslations,
                        surahVersePreferences = surahVersePreferences,
                        listState = listState,
                        onSurahChange = onSurahChange,
                        onSurahVerseClick = {
                            activeBottomSheet = SurahVerseBottomSheet.VerseBottomSheet(it)
                        }
                    )
                }

                is SurahVerseViewModel.InformativeDisplayMode.PageMode -> {
                    LaunchedEffect(Unit) {
                        val index = surahVerses.indexOf(informativeDisplayMode.surahVerse)
                        if (index > surahVerses.size.half()) {
                            pageScrollState.animateScrollTo(pageScrollState.maxValue)
                        }
                    }

                    SurahVersePageMode(
                        modifier = Modifier.weight(1f),
                        surahVerses = surahVerses,
                        page = page,
                        onPageChange = onPageChange,
                        scrollState = pageScrollState,
                        onSurahVerseClick = {
                            activeBottomSheet = SurahVerseBottomSheet.VerseBottomSheet(it)
                        }
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
                        val informativeDisplayMode = when (displayMode) {
                            SurahVersePreferences.DisplayMode.LIST -> {
                                val index = if (pageScrollState.value > pageScrollState.maxValue.half()) {
                                    surahVerses.size.half()
                                } else 0
                                val surahVerse = surahVerses[index]
                                SurahVerseViewModel.InformativeDisplayMode.ListMode(surahVerse)
                            }

                            SurahVersePreferences.DisplayMode.PAGE -> {
                                val surahVerse = surahVerses[listState.firstVisibleItemIndex]
                                SurahVerseViewModel.InformativeDisplayMode.PageMode(surahVerse)
                            }
                        }
                        onDisplayModeClick(informativeDisplayMode)
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
    surahVerseTranslations: List<SurahVerseTranslation>,
    surahVersePreferences: SurahVersePreferences,
    listState: LazyListState,
    onSurahChange: (Int) -> Unit,
    onSurahVerseClick: (SurahVerse) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = surah.number.toIndex(),
        pageCount = { Constants.TOTAL_QURAN_SURAH }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .drop(1)
            .collect { surahNumber ->
                onSurahChange(surahNumber.fromIndex())
            }
    }

    HorizontalPager(
        modifier = modifier,
        reverseLayout = true,
        state = pagerState
    ) { surahNumber ->
        SurahVerseList(
            modifier = Modifier.fillMaxSize(),
            surahVerses = surahVerses.filter { it.surah.number == surahNumber.fromIndex() },
            surahVerseTranslations = surahVerseTranslations,
            surahVersePreferences = surahVersePreferences,
            listState = listState,
            onSurahVerseClick = onSurahVerseClick
        )
    }
}

@Composable
private fun SurahVersePageMode(
    modifier: Modifier = Modifier,
    surahVerses: List<SurahVerse>,
    page: Int,
    scrollState: ScrollState,
    onPageChange: (Int) -> Unit,
    onSurahVerseClick: (SurahVerse) -> Unit
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
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
            surahVerses = surahVerses.filter { it.verse.page == page.fromIndex() },
            onSurahVerseClick = onSurahVerseClick
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

@PhonePreviews
@Composable
private fun SurahVerseScreenPreview() {
    Previews.Preview {
        SurahVerseScreen(
            surah = surahFixture,
            surahVerses = surahVerseFixtures,
            surahVerseTranslations = surahVerseTranslationFixtures,
            page = 1,
            surahVersePreferences = surahVersePreferencesFixture,
            informativeDisplayMode = SurahVerseViewModel.InformativeDisplayMode.ListMode(surahVerseFixtures.first()),
            surahVersePlayerData = null,
            player = null,
            snackBarHostState = SnackbarHostState(),
            onBackClick = {},
            onTranslationLanguageClick = {},
            onDisplayTranslationChange = {},
            onReciterClick = {},
            onPlayVerseAudioClick = {},
            onDisplayModeClick = {},
            onPageChange = {},
            onSurahChange = {}
        )
    }
}