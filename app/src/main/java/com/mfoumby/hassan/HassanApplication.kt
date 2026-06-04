package com.mfoumby.hassan

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.mfoumby.hassan.common.data.commonDataModule
import com.mfoumby.hassan.quran.data.quranDataModule
import com.mfoumby.hassan.quran.data.worker.StartupQuranWorker
import com.mfoumby.hassan.quran.domain.quranDomainModule
import com.mfoumby.hassan.quran.quranModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HassanApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        configureFirestore()

        startKoin {
            androidLogger()
            androidContext(this@HassanApplication)
            modules(
                listOf(
                    appModule,
                    commonDataModule,
                    quranModule,
                    quranDomainModule,
                    quranDataModule
                )
            )
        }

        get<StartupQuranWorker>().run()
    }

    private fun configureFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.clearPersistence()

        val memoryCacheSettings = MemoryCacheSettings
            .newBuilder()
            .build()

        val firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(memoryCacheSettings)
            .build()

        db.firestoreSettings = firestoreSettings
    }
}