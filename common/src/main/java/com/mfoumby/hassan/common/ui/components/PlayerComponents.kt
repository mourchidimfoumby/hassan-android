package com.mfoumby.hassan.common.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util.getStringForTime
import androidx.media3.ui.compose.indicators.ProgressIndicator
import androidx.media3.ui.compose.indicators.TimeText
import com.mfoumby.hassan.common.R
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultPlayerSlider(player: Player?) {
    Column {
        Slider(player)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TimeText(
                player = player,
                timeFormat = TimeFormat.POSITION
            )
            TimeText(
                player = player,
                timeFormat = TimeFormat.DURATION
            )
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@ExperimentalMaterial3Api
@Composable
private fun Slider(player: Player?) {
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    var sliderWidthPx by remember { mutableIntStateOf(0) }

    ProgressIndicator(player, totalTickCount = sliderWidthPx, scope) {
        var isDragging by remember { mutableStateOf(false) }
        var seekPosition by remember { mutableFloatStateOf(0f) }

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { (w, _) -> sliderWidthPx = w }
                .height(dimensionResource(R.dimen.slider_height)),
            value = if (isDragging) seekPosition else currentPositionProgress,
            onValueChange = {
                isDragging = true
                seekPosition = it
            },
            onValueChangeFinished = {
                updateCurrentPositionProgress(seekPosition)
                isDragging = false
            },
            track = { state ->
                SliderDefaults.Track(
                    modifier = Modifier.height(dimensionResource(R.dimen.slider_track_height)),
                    drawStopIndicator = null,
                    thumbTrackGapSize = 0.dp,
                    sliderState = state
                )
            },
            thumb = {
                val size = dimensionResource(R.dimen.slider_thumb_size)
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    thumbSize = DpSize(size, size)
                )
            },
            enabled = changingProgressEnabled,
            interactionSource = interactionSource
        )
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun TimeText(
    player: Player?,
    timeFormat: TimeFormat
) {
    val modifier: Modifier = Modifier
    val scope = rememberCoroutineScope()
    TimeText(
        player = player,
        scope = scope
    ) {
        val text = when (timeFormat) {
            TimeFormat.POSITION -> getStringForTime(currentPositionMs)
            TimeFormat.DURATION -> getStringForTime(durationMs)
        }

        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private enum class TimeFormat {
    POSITION,
    DURATION
}

@PhonePreviews
@Composable
private fun DefaultPlayerSliderPreview() {
    Previews.Preview {
        DefaultPlayerSlider(player = null)
    }
}