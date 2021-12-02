package com.geekbrains.tests.di.modules

import com.geekbrains.tests.presenter.SchedulerProvider
import com.geekbrains.tests.presenter.SearchSchedulerProvider
import org.koin.dsl.module

internal val schedulerModule = module {
    factory<SchedulerProvider> { SearchSchedulerProvider() }
}
