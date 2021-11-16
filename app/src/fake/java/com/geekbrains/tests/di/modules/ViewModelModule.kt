package com.geekbrains.tests.di.modules

import com.geekbrains.tests.presenter.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val viewModelModule = module {
    viewModel {
        SearchViewModel(
            repository = get(named("fake"))
        )
    }
}
