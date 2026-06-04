package com.mfoumby.hassan.common.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mfoumby.hassan.common.ui.theme.HassanTheme

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Composable
fun OptionButton(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ),
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        colors = colors,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit,
    color: IconButtonColors = IconButtonDefaults.iconButtonColors()
) {
    IconButton(
        onClick = onClick,
        colors = color
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = com.mfoumby.hassan.common.R.string.arrow_back_icon_description)
        )
    }
}


/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
private fun PrimaryButtonPreview() {
    HassanTheme {
        PrimaryButton(
            text = "Primary Button",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun OptionButtonPreview() {
    HassanTheme {
        OptionButton(
            onClick = {}
        )
    }
}
