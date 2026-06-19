package com.mfoumby.hassan.common.data.model

import com.google.firebase.firestore.PropertyName
import com.mfoumby.hassan.common.data.field.TranslationField

data class RemoteTranslationLanguage(
    @get:PropertyName(TranslationField.Remote.LANGUAGE)
    @set:PropertyName(TranslationField.Remote.LANGUAGE)
    var language: String = ""
)