package com.mfoumby.hassan.common.data

import android.util.Log.e
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val OKHTTP_CLIENT_QUALIFIER = named("okhttp_client_qualifier")
private val BACKGROUND_SCOPE = named("BackgroundScope")

val commonDataModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
            SupervisorJob() +
                    Dispatchers.IO +
                    CoroutineExceptionHandler { _, throwable ->
                        e(javaClass.simpleName, "Uncaught error in backgroundScope", throwable)
                    }
        )
    }

    single<OkHttpClient>(OKHTTP_CLIENT_QUALIFIER) {
        OkHttpClient.Builder().build()
    }
}