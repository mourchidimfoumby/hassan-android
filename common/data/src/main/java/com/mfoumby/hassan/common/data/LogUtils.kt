package com.mfoumby.hassan.common.data

import android.util.Log

fun Any.d(message: String) {
    Log.d(javaClass.simpleName, message)
}

fun Any.e(message: String?, throwable: Throwable? = null) {
    Log.e(javaClass.simpleName, message, throwable)
}

fun Any.i(message: String) {
    Log.i(javaClass.simpleName, message)
}

fun Any.w(message: String) {
    Log.w(javaClass.simpleName, message)
}