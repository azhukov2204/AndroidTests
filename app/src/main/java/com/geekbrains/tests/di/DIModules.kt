package com.geekbrains.tests.di

import com.geekbrains.tests.di.modules.presenterModule
import com.geekbrains.tests.di.modules.repositoryModule

internal val diModules = listOf(
    repositoryModule,
    presenterModule
)
