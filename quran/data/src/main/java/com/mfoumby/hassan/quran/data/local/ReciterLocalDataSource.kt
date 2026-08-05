package com.mfoumby.hassan.quran.data.local

import com.mfoumby.hassan.quran.data.mapper.toLocal
import com.mfoumby.hassan.quran.data.mapper.toReciter
import com.mfoumby.hassan.quran.domain.entity.Reciter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReciterLocalDataSource(
    private val reciterDataStore: ReciterDataStore
) {
    private val dispatcher = Dispatchers.IO

    suspend fun getReciters(): List<Reciter> = withContext(dispatcher) {
        reciterDataStore.getReciters().map { it.toReciter() }
    }

    suspend fun setReciter(reciters: List<Reciter>) {
        withContext(dispatcher) {
            reciterDataStore.setReciters(reciters.map { it.toLocal() })
        }
    }
}