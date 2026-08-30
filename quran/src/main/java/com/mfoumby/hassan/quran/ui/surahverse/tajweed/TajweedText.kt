package com.mfoumby.hassan.quran.ui.surahverse.tajweed

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.quran.domain.verseFixtures3

@Composable
fun TajweedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val t = TajweedUtils.getTajweeds(text)
//    Text(t.joinToString())
    Text(
        text = buildAnnotatedString {
            append(text)
            t.forEach { tajweed ->
                addStyle(
                    style = SpanStyle(color = Color(tajweed.type.color)),
                    start = tajweed.startIndex,
                    end = tajweed.endIndex
                )
            }
        },
        modifier = modifier,
        style = style,
        fontSize = fontSize
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TajweedTextPreview() {
    Previews.Preview {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TajweedText(text = verseFixtures3.get(3).text)
        }
    }
}