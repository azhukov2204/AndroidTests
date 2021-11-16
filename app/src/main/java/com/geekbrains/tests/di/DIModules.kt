package com.geekbrains.tests.di

import com.geekbrains.tests.di.modules.presenterModule
import com.geekbrains.tests.di.modules.repositoryModule
import com.geekbrains.tests.di.modules.schedulerModule
import com.geekbrains.tests.di.modules.viewModelModule

internal val diModules = listOf(
    repositoryModule,
    presenterModule,
    schedulerModule,
    viewModelModule
)
