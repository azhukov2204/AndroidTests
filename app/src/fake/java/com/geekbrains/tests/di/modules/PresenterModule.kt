package com.geekbrains.tests.di.modules

import com.geekbrains.tests.presenter.search.SearchPresenterContract
import com.geekbrains.tests.presenter.search.SearchPresenterImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val presenterModule = module {
    factory<SearchPresenterContract> {
        SearchPresenterImpl(
            repository = get(named("fake"))
        )
    }
}
