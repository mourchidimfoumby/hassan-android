package com.mfoumby.hassan.quran.ui.surahverse.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mfoumby.hassan.common.ui.BottomSheetItemValue
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.extension.mediumSpacing
import com.mfoumby.hassan.quran.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahVerseBottomSheet(
    onDismissRequest: () -> Unit,
    onPlayVerseAudioClick: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            itemValues.forEach { value ->
                val action = when (value.first) {
                    ItemType.PLAY -> onPlayVerseAudioClick
                }

                ListItem(
                    modifier = Modifier.clickable(onClick = action),
                    headlineContent = { Text(text = stringResource(value.second.textRes)) },
                    leadingContent = {
                        Icon(
                            painter = painterResource(value.second.iconRes),
                            contentDescription = null
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }
    }
}

private val itemValues = listOf(
    ItemType.PLAY to BottomSheetItemValue(
        textRes = R.string.listen,
        iconRes = R.drawable.ic_outline_play_arrow
    )
)

private enum class ItemType {
    PLAY
}

@PhonePreviews
@Composable
private fun SurahVerseBottomSheetPreview() {
    Previews.Preview {
        SurahVerseBottomSheet(
            onDismissRequest = {},
            onPlayVerseAudioClick = {}
        )
    }
}