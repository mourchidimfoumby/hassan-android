package com.mfoumby.hassan.common.domain.usecase

import com.mfoumby.hassan.common.domain.entity.Language
import java.util.Locale

class GetCurrentLanguageUseCase {
    fun execute(): Language = Locale.getDefault().language.toString().let(Language.Companion::fromLanguageCode)
}