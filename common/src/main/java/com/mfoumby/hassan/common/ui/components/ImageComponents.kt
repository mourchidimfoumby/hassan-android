package com.mfoumby.hassan.common.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.theme.loadingImageBackground

@Composable
fun SimpleAsyncImage(
    model: Any,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        error = ColorPainter(MaterialTheme.colorScheme.loadingImageBackground)
    )
}

@PhonePreviews
@Composable
private fun SimpleAsyncImagePreview() {
    Previews.Preview {
        SimpleAsyncImage(model = "")
    }
}