package com.mfoumby.hassan.common.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.theme.bismillah

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun BismillahText(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = "5",
        style = MaterialTheme.typography.bismillah
    )
}

@Preview
@Composable
private fun SectionTitlePreview() {
    Previews.Preview {
        SectionTitle(text = "Section title")
    }
}

@Preview
@Composable
private fun BismillahTextPreview() {
    Previews.Preview {
        BismillahText()
    }
}