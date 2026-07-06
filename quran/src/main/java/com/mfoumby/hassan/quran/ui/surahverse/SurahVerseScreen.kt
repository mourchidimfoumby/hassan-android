package com.mfoumby.hassan.quran.ui.surahverse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.mfoumby.hassan.common.SingleUiEvent
import com.mfoumby.hassan.common.domain.NumberFormatUtils
import com.mfoumby.hassan.common.snackbarLauncher
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.BackTopBar
import com.mfoumby.hassan.common.ui.components.VerticalScrollBarIndicator
import com.mfoumby.hassan.common.ui.theme.bodyUthmanic
import com.mfoumby.hassan.common.ui.theme.padding
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVersePlayerData
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.surahFixture
import com.mfoumby.hassan.quran.domain.surahVerseFixtures
import com.mfoumby.hassan.quran.domain.surahVersePreferencesFixture
import com.mfoumby.hassan.quran.ui.surahverse.components.DownloadAudioDialog
import com.mfoumby.hassan.quran.ui.surahverse.components.DownloadingAudioDialog
import com.mfoumby.hassan.quran.ui.surahverse.components.SurahVerseBottomSheet
import com.mfoumby.hassan.quran.ui.surahverse.components.SurahVersePlayer
import com.mfoumby.hassan.quran.ui.surahverse.components.SurahVerseSettingsBottomSheet
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

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
    val player = remember { ExoPlayer.Builder(context).build() }
    val surahVerseAudios = uiState.surahVersePlayerData?.surahVerseAudios
    val surahVersePlayerData = uiState.surahVersePlayerData
    val snackBarHostState = remember { SnackbarHostState() }
    val showSnackbar = snackbarLauncher(snackBarHostState)
    var activeDialog by remember { mutableStateOf<SurahVerseDialog?>(null) }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                SurahVerseViewModel.SurahVerseUiEvent.AudioDownloadSuccess -> {
                    viewModel.refreshSurahVerseAudios()
                    activeDialog = null
                }

                is SurahVerseViewModel.SurahVerseUiEvent.AudioDownloadError -> {
                    activeDialog = null
                    showSnackbar(context.getString(it.messageId))
                }

                SurahVerseViewModel.SurahVerseUiEvent.DownloadAudioRequest -> {
                    activeDialog = SurahVerseDialog.DownloadAudioDialog
                }

                is SingleUiEvent.Error -> showSnackbar(context.getString(it.messageId))
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
            surahVersePreferences = uiState.surahVersePreferences!!,
            surahVersePlayerData = surahVersePlayerData,
            player = player,
            snackBarHostState = snackBarHostState,
            onBackClick = onBackClick,
            onTranslationLanguageClick = onTranslationLanguageClick,
            onDisplayTranslationChange = viewModel::onDisplayTranslationChange,
            onReciterClick = onReciterClick,
            onPlayVerseAudioClick = viewModel::onPlayAudio
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SurahVerseScreen(
    surah: Surah,
    surahVerses: List<SurahVerse>,
    surahVersePreferences: SurahVersePreferences,
    surahVersePlayerData: SurahVersePlayerData?,
    player: Player?,
    snackBarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onTranslationLanguageClick: () -> Unit,
    onDisplayTranslationChange: (Boolean) -> Unit,
    onReciterClick: () -> Unit,
    onPlayVerseAudioClick: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    var activeBottomSheet by remember { mutableStateOf<SurahVerseBottomSheet?>(null) }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = surah.transliteration,
                actions = {
                    IconButton(onClick = { activeBottomSheet = SurahVerseBottomSheet.SettingsBottomSheet }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
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
                LazyColumn(state = listState) {
                    items(surahVerses.size) { index ->
                        val surahVerse = surahVerses[index]
                        if (index == 0) {
                            HorizontalDivider()
                        }
                        SurahVerseCell(
                            surahVerse = surahVerse,
                            displayTranslation = surahVersePreferences.displayTranslation,
                            onClick = {
                                activeBottomSheet =
                                    SurahVerseBottomSheet.VerseBottomSheet(surahVerse)
                            }
                        )
                        HorizontalDivider()
                    }
                }

                VerticalScrollBarIndicator(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    state = listState,
                    itemsCount = surahVerses.size
                )
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
    }

    when (val bottomSheet = activeBottomSheet) {
        SurahVerseBottomSheet.SettingsBottomSheet -> {
            SurahVerseSettingsBottomSheet(
                onDismissRequest = { activeBottomSheet = null },
                translationLanguage = surahVersePreferences.translationLanguage,
                displayTranslation = surahVersePreferences.displayTranslation,
                reciter = surahVersePreferences.reciter,
                onTranslationLanguageClick = {
                    activeBottomSheet = null
                    onTranslationLanguageClick()
                },
                onDisplayTranslationChange = onDisplayTranslationChange,
                onReciterClick = {
                    activeBottomSheet = null
                    onReciterClick()
                }
            )
        }

        is SurahVerseBottomSheet.VerseBottomSheet -> {
            SurahVerseBottomSheet(
                onDismissRequest = { activeBottomSheet = null },
                onPlayVerseAudioClick = {
                    activeBottomSheet = null
                    onPlayVerseAudioClick(bottomSheet.surahVerse.verseNumber)
                }
            )
        }

        else -> Unit
    }
}

@Composable
private fun SurahVerseCell(
    surahVerse: SurahVerse,
    displayTranslation: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(MaterialTheme.padding.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.smallMedium)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = surahVerse.text + " " + NumberFormatUtils.toArabic(surahVerse.verseNumber),
            style = MaterialTheme.typography.bodyUthmanic
        )

        if (displayTranslation) {
            surahVerse.translation?.let {
                Text(text = surahVerse.verseNumber.toString() + ". " + it)
            }
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

@PhonePreviews
@Composable
private fun SurahVerseScreenPreview() {
    Previews.Preview {
        SurahVerseScreen(
            surah = surahFixture,
            surahVerses = surahVerseFixtures,
            surahVersePreferences = surahVersePreferencesFixture,
            surahVersePlayerData = null,
            player = null,
            snackBarHostState = SnackbarHostState(),
            onBackClick = {},
            onTranslationLanguageClick = {},
            onDisplayTranslationChange = {},
            onReciterClick = {},
            onPlayVerseAudioClick = {}
        )
    }
}