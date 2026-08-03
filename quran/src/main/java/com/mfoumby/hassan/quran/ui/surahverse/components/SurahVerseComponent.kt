package com.mfoumby.hassan.quran.ui.surahverse.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.BismillahText
import com.mfoumby.hassan.common.ui.theme.padding
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.surahFixture2
import com.mfoumby.hassan.quran.ui.SurahMetadata

@Composable
fun SurahHeader(surah: Surah) {
    Column {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .height(60.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier.size(
                        width = 200.dp,
                        height = 50.dp
                    ),
                    painter = painterResource(R.drawable.surah_outline),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )

                Icon(
                    modifier = Modifier.size(
                        width = 60.dp,
                        height = 50.dp
                    ),
                    painter = painterResource(SurahMetadata.getSurahImageResId(surah.number)),
                    contentDescription = null,
                )
            }
        }

        if (surah.number != 1) {
            BismillahText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.padding.extraSmall)
            )
        }
    }
}

@PhonePreviews
@Composable
private fun SurahHeaderPreview() {
    Previews.Preview {
        SurahHeader(surah = surahFixture2)
    }
}