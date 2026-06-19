package com.mfoumby.hassan.common.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.mfoumby.hassan.common.ui.theme.HassanTheme

object Previews {
    @Composable
    fun Preview(content: @Composable () -> Unit) {
        HassanTheme {
            Surface {
                content()
            }
        }
    }
}