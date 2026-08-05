package com.mfoumby.hassan.quran.data.repository

import com.mfoumby.hassan.quran.data.local.ReciterLocalDataSource
import com.mfoumby.hassan.quran.data.remote.ReciterRemoteDataSource
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.repository.ReciterRepository

class ReciterRepositoryImpl(
    private val reciterLocalDataSource: ReciterLocalDataSource,
    private val reciterRemoteDataSource: ReciterRemoteDataSource
): ReciterRepository {
    override suspend fun getReciters(): List<Reciter> = reciterLocalDataSource.getReciters()

    override suspend fun fetchReciterCount(): Int = reciterRemoteDataSource.getReciterCount()


    override suspend fun downloadReciters() {
        reciterRemoteDataSource.getReciters().let {
            reciterLocalDataSource.setReciter(it)
        }
    }
}