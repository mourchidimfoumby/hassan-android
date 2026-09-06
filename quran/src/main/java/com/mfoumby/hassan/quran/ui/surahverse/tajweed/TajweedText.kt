package com.mfoumby.hassan.quran.ui.surahverse.tajweed

import android.content.res.Configuration
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.theme.bodyUthmanic
import com.mfoumby.hassan.quran.domain.verseFixture2

@Composable
fun TajweedText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = buildAnnotatedString {
            append(text)
            TajweedUtils.getTajweed(text).forEach { tajweed ->
                addStyle(
                    style = SpanStyle(color = Color(tajweed.type.color)),
                    start = tajweed.startIndex,
                    end = tajweed.endIndex
                )
            }
        },
        modifier = modifier,
        style = MaterialTheme.typography.bodyUthmanic
    )
}

@Composable
fun TajweedText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default
) {
    BasicText(
        modifier = modifier,
        text = buildAnnotatedString {
            append(text)
            TajweedUtils.getTajweed(text.text).forEach { tajweed ->
                addStyle(
                    style = SpanStyle(color = Color(tajweed.type.color)),
                    start = tajweed.startIndex,
                    end = tajweed.endIndex
                )
            }
        },
        style = style
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TajweedTextPreview() {
    Previews.Preview {
        TajweedText(text = verseFixture2.text)
    }
}