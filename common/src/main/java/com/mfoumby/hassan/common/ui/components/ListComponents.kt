package com.mfoumby.hassan.common.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mfoumby.hassan.common.ui.components.scrollbar.DraggableScrollbar
import com.mfoumby.hassan.common.ui.components.scrollbar.rememberDraggableScroller
import com.mfoumby.hassan.common.ui.components.scrollbar.scrollbarState

@Composable
fun VerticalScrollBarIndicator(
    modifier: Modifier = Modifier,
    state: LazyListState,
    itemsCount: Int
) {
    state.DraggableScrollbar(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 2.dp),
        state = state.scrollbarState(itemsAvailable = itemsCount),
        orientation = Orientation.Vertical,
        onThumbMoved = state.rememberDraggableScroller(itemsAvailable = itemsCount)
    )
}