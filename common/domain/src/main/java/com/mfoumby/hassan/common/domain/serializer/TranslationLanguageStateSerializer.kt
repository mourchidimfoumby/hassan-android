package com.mfoumby.hassan.common.domain.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage.TranslationLanguageState
import java.lang.reflect.Type

object TranslationLanguageStateSerializer: JsonSerializer<TranslationLanguageState>, JsonDeserializer<TranslationLanguageState> {
    private const val TYPE_PROPERTY = "type"

    override fun serialize(
        src: TranslationLanguageState,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return when (src) {
            is TranslationLanguageState.NotDownloaded -> context.serialize(src).asJsonObject
                .apply { addProperty(TYPE_PROPERTY, TranslationLanguageState.NotDownloaded.TYPE) }

            is TranslationLanguageState.Downloading -> context.serialize(src).asJsonObject
                .apply { addProperty(TYPE_PROPERTY, TranslationLanguageState.Downloading.TYPE) }

            is TranslationLanguageState.Downloaded -> context.serialize(src).asJsonObject
                .apply { addProperty(TYPE_PROPERTY, TranslationLanguageState.Downloaded.TYPE) }
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): TranslationLanguageState {
        return when (val type = json.asJsonObject.get(TYPE_PROPERTY).asString) {
            TranslationLanguageState.NotDownloaded.TYPE -> context.deserialize(json, TranslationLanguageState.NotDownloaded::class.java)
            TranslationLanguageState.Downloading.TYPE -> context.deserialize(json, TranslationLanguageState.Downloading::class.java)
            TranslationLanguageState.Downloaded.TYPE -> context.deserialize(json, TranslationLanguageState.Downloaded::class.java)
            else -> throw JsonParseException("Unrecognized mission state type : $type")
        }
    }
}