package com.mfoumby.hassan.common.domain.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun CoroutineScope.launchAll(vararg blocks: suspend () -> Unit): List<Job> {
    return blocks.map { block ->
        launch { block() }
    }
}