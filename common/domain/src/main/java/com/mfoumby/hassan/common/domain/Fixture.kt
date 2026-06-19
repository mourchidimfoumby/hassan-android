package com.mfoumby.hassan.common.domain

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage

val translationLanguageFixture = TranslationLanguage(
    language = Language.ENGLISH,
    state = TranslationLanguage.TranslationLanguageState.Downloaded
)