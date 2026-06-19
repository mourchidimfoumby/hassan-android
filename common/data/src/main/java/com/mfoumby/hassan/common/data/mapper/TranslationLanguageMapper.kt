package com.mfoumby.hassan.common.data.mapper

import com.google.gson.GsonBuilder
import com.mfoumby.hassan.common.data.model.LocalTranslationLanguage
import com.mfoumby.hassan.common.data.model.RemoteTranslationLanguage
import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage.TranslationLanguageState
import com.mfoumby.hassan.common.domain.serializer.TranslationLanguageStateSerializer

private val gson = GsonBuilder()
    .registerTypeAdapter(TranslationLanguageState::class.java, TranslationLanguageStateSerializer)
    .create()

fun TranslationLanguage.toLocal() = LocalTranslationLanguage(
    language = language.name,
    state = gson.toJson(state, TranslationLanguageState::class.java)
)

fun LocalTranslationLanguage.toTranslationLanguage() = TranslationLanguage(
    language = Language.valueOf(language),
    state = gson.fromJson(state, TranslationLanguageState::class.java)
)

fun RemoteTranslationLanguage.toLanguage() = Language.fromLanguageCode(language)