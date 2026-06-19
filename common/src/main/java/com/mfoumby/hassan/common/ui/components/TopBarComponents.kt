package com.mfoumby.hassan.common.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTopBar(
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1
            )
        },
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            BackButton(onClick = onBackClick)
        },
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun TitleTopBarPreview() {
    Previews.Preview {
        Surface {
            TitleTopBar(title = "Title")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PhonePreviews
@Composable
private fun BackTopBarPreview() {
    Previews.Preview {
        Surface {
            BackTopBar(
                onBackClick = {},
                title = "Title"
            )
        }
    }
}