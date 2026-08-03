package com.mfoumby.hassan.common.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mfoumby.hassan.common.R
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews

@Composable
fun SimpleDialog(
    modifier: Modifier = Modifier,
    text: String,
    title: String? = null,
    confirmText: String = stringResource(id = R.string.accept),
    cancelText: String = stringResource(id = R.string.cancel),
    critical: Boolean = false,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        title = title?.let { { Text(text = title) } },
        text = { Text(text = text) },
        onDismissRequest = onCancel,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    color = if (critical) MaterialTheme.colorScheme.error else Color.Unspecified
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = cancelText)
            }
        }
    )
}


@PhonePreviews
@Composable
private fun SimpleDialogPreview() {
    Previews.Preview {
        SimpleDialog(
            text = "There is the text area",
            title = "Simple dialog",
            confirmText = "Confirm",
            cancelText = "Cancel",
            onConfirm = {},
            onCancel = {},
        )
    }
}

@PhonePreviews
@Composable
private fun CriticalDialogPreview() {
    Previews.Preview {
        SimpleDialog(
            text = "Do you want to do this sensible action ?",
            title = "Sensible action",
            confirmText = "Delete",
            cancelText = "Cancel",
            critical = true,
            onConfirm = {},
            onCancel = {}
        )
    }
}