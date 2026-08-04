package com.mfoumby.hassan.quran.ui.surahverse.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.SimpleDialog
import com.mfoumby.hassan.common.ui.extension.mediumSpacing
import com.mfoumby.hassan.common.ui.theme.padding
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.reciterFixture
import com.mfoumby.hassan.quran.domain.surahFixture

@Composable
fun DownloadAudioDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    SimpleDialog(
        text = stringResource(R.string.download_audio_dialog_text),
        confirmText = stringResource(com.mfoumby.hassan.common.R.string.download),
        onConfirm = onConfirm,
        onCancel = onCancel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadingAudioDialog(
    progress: Float,
    surah: Surah,
    reciter: Reciter,
    currentStep: Int,
    totalSteps: Int,
    onCancelDownloadingClick: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        DownloadingAudioDialogContent(
            progress = progress,
            surah = surah,
            reciter = reciter,
            currentStep = currentStep,
            totalSteps = totalSteps,
            onCancelDownloadingClick = onCancelDownloadingClick
        )
    }
}

@Composable
private fun DownloadingAudioDialogContent(
    progress: Float,
    surah: Surah,
    reciter: Reciter,
    currentStep: Int,
    totalSteps: Int,
    onCancelDownloadingClick: () -> Unit
) {
    Surface(shape = MaterialTheme.shapes.extraLarge) {
        Column(
            modifier = Modifier.padding(MaterialTheme.padding.large),
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            val step = if (totalSteps > 1) "(${currentStep}/$totalSteps) \u2022 " else ""

            Text(
                text = step +
                        "${stringResource(R.string.surah)} " +
                        surah.transliteration +
                        " - " + reciter.name,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${stringResource(com.mfoumby.hassan.common.R.string.downloading)} " +
                        "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall
            )


            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { progress }
            )

            Box(
                modifier = Modifier.align(Alignment.End)
            ) {
                TextButton(onClick = onCancelDownloadingClick) {
                    Text(text = stringResource(com.mfoumby.hassan.common.R.string.cancel))
                }
            }
        }
    }
}

@PhonePreviews
@Composable
private fun DownloadAudioDialogPreview() {
    Previews.Preview {
        DownloadAudioDialog(
            onConfirm = {},
            onCancel = {}
        )
    }
}

@PhonePreviews
@Composable
private fun DownloadingAudioDialogContentPreview() {
    Previews.Preview {
        DownloadingAudioDialogContent(
            progress = 0.5f,
            surah = surahFixture,
            reciter = reciterFixture,
            currentStep = 1,
            totalSteps = 4,
            onCancelDownloadingClick = {}
        )
    }
}