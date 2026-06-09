package com.mfoumby.hassan.common.domain

import com.mfoumby.hassan.common.domain.usecase.LanguageUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val commonDomainModule = module {
    factoryOf(::LanguageUseCase)
}