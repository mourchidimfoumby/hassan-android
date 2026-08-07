package com.mfoumby.hassan.quran.ui.surahverse.surahversetranslationlanguage

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import com.mfoumby.hassan.common.R
import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage.TranslationLanguageState
import com.mfoumby.hassan.common.getStringResId
import com.mfoumby.hassan.common.snackbarLauncher
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.TranslationLanguageListComponent
import com.mfoumby.hassan.common.ui.components.BackTopBar
import com.mfoumby.hassan.common.ui.components.SimpleDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun SurahVerseTranslationLanguageDestination(
    onBackClick: () -> Unit,
    viewModel: SurahVerseTranslationLanguageViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val showSnackbar = snackbarLauncher(snackBarHostState)
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SurahVerseTranslationLanguageViewModel.SurahVerseTranslationUiEvent.SurahVerseTranslationDownloadError -> {
                    val language = resources.getString(event.translationLanguage.language.getStringResId())
                    showSnackbar(resources.getString(R.string.download_translation_error, language))
                }
            }
        }
    }

    if (!uiState.isLoading) {
        SurahVerseTranslationLanguageScreen(
            selectedLanguage = uiState.preferences!!.translationLanguage,
            supportedLanguages = uiState.translationLanguages!!,
            snackBarHostState = snackBarHostState,
            onTranslationLanguageSelect = viewModel::onTranslationLanguageSelect,
            onDeleteTranslationLanguageClick = viewModel::onDeleteTranslationLanguage,
            onCancelTranslationLanguageDownloadClick = viewModel::onCancelTranslationLanguageDownload,
            onBackClick = onBackClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SurahVerseTranslationLanguageScreen(
    selectedLanguage: Language?,
    supportedLanguages: List<TranslationLanguage>,
    snackBarHostState: SnackbarHostState,
    onTranslationLanguageSelect: (TranslationLanguage) -> Unit,
    onDeleteTranslationLanguageClick: (TranslationLanguage) -> Unit,
    onCancelTranslationLanguageDownloadClick: (TranslationLanguage) -> Unit,
    onBackClick: () -> Unit
) {
    var activeDialog by remember { mutableStateOf<SurahVerseTranslationLanguageDialog?>(null) }

    when (val dialog = activeDialog) {
        is SurahVerseTranslationLanguageDialog.DownloadLanguageDialog -> {
            SimpleDialog(
                text = stringResource(id = R.string.download_language_dialog_text),
                confirmText = stringResource(id = R.string.download),
                onConfirm = {
                    activeDialog = null
                    onTranslationLanguageSelect(dialog.translationLanguage)
                },
                onCancel = { activeDialog = null }
            )
        }

        is SurahVerseTranslationLanguageDialog.DeleteLanguageDialog -> {
            SimpleDialog(
                text = stringResource(id = R.string.delete_language_dialog_text),
                confirmText = stringResource(id = R.string.delete),
                onConfirm = {
                    activeDialog = null
                    onDeleteTranslationLanguageClick(dialog.translationLanguage)
                },
                onCancel = { activeDialog = null }
            )
        }

        else -> Unit
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.select_translation),
                onBackClick = onBackClick
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState) {
                Snackbar(it)
            }
        }
    ) { innerPadding ->
        TranslationLanguageListComponent(
            modifier = Modifier.padding(innerPadding),
            selectedLanguage = selectedLanguage,
            translationLanguages = supportedLanguages,
            onTranslationLanguageSelect = {
                when (it.state) {
                    is TranslationLanguageState.NotDownloaded -> activeDialog = SurahVerseTranslationLanguageDialog.DownloadLanguageDialog(it)
                    else -> onTranslationLanguageSelect(it)
                }
            },
            onDeleteTranslationLanguageClick = {
                activeDialog = SurahVerseTranslationLanguageDialog.DeleteLanguageDialog(it)
            },
            onCancelTranslationLanguageDownloadClick = onCancelTranslationLanguageDownloadClick
        )
    }
}

private sealed class SurahVerseTranslationLanguageDialog {
    data class DownloadLanguageDialog(val translationLanguage: TranslationLanguage): SurahVerseTranslationLanguageDialog()
    data class DeleteLanguageDialog(val translationLanguage: TranslationLanguage): SurahVerseTranslationLanguageDialog()
}

@PhonePreviews
@Composable
private fun SurahVerseTranslationLanguageScreenPreview() {
    Previews.Preview {
        SurahVerseTranslationLanguageScreen(
            selectedLanguage = Language.ENGLISH,
            supportedLanguages = listOf(
                TranslationLanguage(Language.ENGLISH, TranslationLanguageState.Downloaded),
                TranslationLanguage(Language.FRENCH, TranslationLanguageState.NotDownloaded)
            ),
            snackBarHostState = SnackbarHostState(),
            onTranslationLanguageSelect = {},
            onCancelTranslationLanguageDownloadClick = {},
            onDeleteTranslationLanguageClick = {},
            onBackClick = {}
        )
    }
}