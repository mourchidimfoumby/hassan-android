package com.mfoumby.hassan.common

import androidx.annotation.StringRes

interface SingleUiEvent {
    data class Success(@param:StringRes val messageId: Int = -1): SingleUiEvent
    data class Error(@param:StringRes val messageId: Int): SingleUiEvent
}