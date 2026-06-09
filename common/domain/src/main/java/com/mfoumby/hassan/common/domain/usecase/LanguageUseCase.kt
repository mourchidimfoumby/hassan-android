package com.mfoumby.hassan.common.domain.usecase

import com.mfoumby.hassan.common.domain.entity.Language
import java.util.Locale

class LanguageUseCase {
    fun getCurrentLanguage(): Language =
        Locale.getDefault().language.toString().let(Language.Companion::fromLanguageCode)
}