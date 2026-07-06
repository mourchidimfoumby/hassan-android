package com.mfoumby.hassan.quran.ui.surahverse.components

import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.media3.common.Player
import androidx.media3.ui.compose.material3.buttons.NextButton
import androidx.media3.ui.compose.material3.buttons.PlayPauseButton
import androidx.media3.ui.compose.material3.buttons.PreviousButton
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.DefaultPlayerSlider
import com.mfoumby.hassan.common.ui.extension.smallSpacing
import com.mfoumby.hassan.common.ui.theme.padding
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import com.mfoumby.hassan.quran.domain.reciterFixture
import com.mfoumby.hassan.quran.domain.surahFixture
import com.mfoumby.hassan.quran.domain.surahVerseAudioFixture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahVersePlayer(
    player: Player?,
    surah: Surah,
    surahVerseAudio: SurahVerseAudio,
    reciter: Reciter,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(MaterialTheme.padding.medium),
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        HeadlineSection(
            player = player,
            surah = surah,
            surahVerseAudio = surahVerseAudio,
            reciter = reciter
        )
        DefaultPlayerSlider(player = player)
    }
}

@Composable
private fun HeadlineSection(
    player: Player?,
    surah: Surah,
    surahVerseAudio: SurahVerseAudio,
    reciter: Reciter
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE,
                    animationMode = MarqueeAnimationMode.Immediately,
                    initialDelayMillis = 1000,
                    repeatDelayMillis = 1500
                ),
                text = "${stringResource(R.string.surah)} ${surah.transliteration}" +
                        " - " +
                        "${stringResource(R.string.verse)} ${surahVerseAudio.verseNumber}"
            )

            Text(
                text = reciter.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(horizontalArrangement = Arrangement.Center) {
            PreviousButton(player)
            PlayPauseButton(player)
            NextButton(player)
        }
    }
}

@PhonePreviews
@Composable
private fun SurahVersePlayerPreview() {
    Previews.Preview {
        SurahVersePlayer(
            player = null,
            surah = surahFixture,
            surahVerseAudio = surahVerseAudioFixture,
            reciter = reciterFixture
        )
    }
}
