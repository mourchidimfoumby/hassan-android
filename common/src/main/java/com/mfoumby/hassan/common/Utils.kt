package com.mfoumby.hassan.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun snackbarLauncher(
    snackbarHostState: SnackbarHostState
): (String) -> Unit {
    val scope = rememberCoroutineScope()
    return { message ->
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }
}