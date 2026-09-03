package com.mfoumby.hassan.common.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.scrollbar.DraggableScrollbar
import com.mfoumby.hassan.common.ui.components.scrollbar.rememberDraggableScroller
import com.mfoumby.hassan.common.ui.components.scrollbar.scrollbarState

@Composable
fun SimpleLazyColumn(
    itemCount: Int,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit
) {
    Box(modifier = modifier) {
        LazyColumn(
            state = state,
            content = content
        )

        VerticalScrollBarIndicator(
            modifier = Modifier.align(Alignment.CenterEnd),
            state = state,
            itemCount = itemCount
        )
    }
}

@Composable
private fun VerticalScrollBarIndicator(
    modifier: Modifier = Modifier,
    state: LazyListState,
    itemCount: Int
) {
    state.DraggableScrollbar(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 2.dp),
        state = state.scrollbarState(itemsAvailable = itemCount),
        orientation = Orientation.Vertical,
        onThumbMoved = state.rememberDraggableScroller(itemsAvailable = itemCount)
    )
}

@Preview
@Composable
private fun SimpleLazyColumnPreview() {
    Previews.Preview {
        SimpleLazyColumn(itemCount = 100) {
            items(100) {
                Text(text = "Item $it")
            }
        }
    }
}