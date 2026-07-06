package com.mfoumby.hassan.quran.data.mapper

import com.mfoumby.hassan.common.data.BuildConfig
import com.mfoumby.hassan.quran.data.model.LocalReciter
import com.mfoumby.hassan.quran.data.model.RemoteReciter
import com.mfoumby.hassan.quran.domain.entity.Reciter

fun LocalReciter.toReciter() = Reciter(
    id = reciterId,
    name = name,
    imageUrl = imageUrl(reciterId, imageName)
)

fun RemoteReciter.toReciter() = Reciter(
    id = reciterId,
    name = name,
    imageUrl = imageUrl(reciterId, imageName)
)

fun Reciter.toLocal() = LocalReciter(
    reciterId = id,
    name = name,
    imageName = imageUrl.substringAfterLast("/")
)

private fun imageUrl(id: String, imageName: String): String =
    "${BuildConfig.ORACLE_BUCKET_URL}/reciters/$id/$imageName"