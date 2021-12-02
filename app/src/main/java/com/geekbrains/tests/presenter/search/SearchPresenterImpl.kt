package com.geekbrains.tests.presenter.search

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.presenter.SchedulerProvider
import com.geekbrains.tests.repository.RepositoryCallback
import com.geekbrains.tests.repository.RepositoryContract
import com.geekbrains.tests.view.search.ViewSearchContract
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response

/**
 * В архитектуре MVP все запросы на получение данных адресуются в Репозиторий.
 * Запросы могут проходить через Interactor или UseCase, использовать источники
 * данных (DataSource), но суть от этого не меняется.
 * Непосредственно Презентер отвечает за управление потоками запросов и ответов,
 * выступая в роли регулировщика движения на перекрестке.
 */

internal class SearchPresenterImpl internal constructor(
    private val repository: RepositoryContract,
    private val appSchedulerProvider: SchedulerProvider
) : SearchPresenterContract, RepositoryCallback {

    private var viewContract: ViewSearchContract? = null

    private val compositeDisposable = CompositeDisposable()

    override fun attachView(view: ViewSearchContract) {
        if (view != viewContract) {
            viewContract = view
        }
    }

    override fun detachView(view: ViewSearchContract) {
        if (view == viewContract) {
            viewContract = null
        }
        compositeDisposable.dispose()
    }

    override fun searchGitHub(searchQuery: String) {

        val disposable = repository.searchGithub(searchQuery)
            .subscribeOn(appSchedulerProvider.io())
            .observeOn(appSchedulerProvider.ui())
            .doOnSubscribe { viewContract?.displayLoading(true) }
            .doOnTerminate { viewContract?.displayLoading(false) }
            .subscribe({ searchResponse ->

                val searchResults = searchResponse.searchResults
                val totalCount = searchResponse.totalCount
                if (searchResults != null && totalCount != null) {
                    viewContract?.displaySearchResults(
                        searchResults,
                        totalCount
                    )
                } else {
                    viewContract?.displayError("Search results or total count are null")
                }
            },
                { e -> viewContract?.displayError(e.message ?: "Response is null or unsuccessful") })

        compositeDisposable.add(disposable)
    }

    override fun handleGitHubResponse(response: Response<SearchResponse?>?) {
        viewContract?.displayLoading(false)
        if (response != null && response.isSuccessful) {
            val searchResponse = response.body()
            val searchResults = searchResponse?.searchResults
            val totalCount = searchResponse?.totalCount
            if (searchResults != null && totalCount != null) {
                viewContract?.displaySearchResults(
                    searchResults,
                    totalCount
                )
            } else {
                viewContract?.displayError("Search results or total count are null")
            }
        } else {
            viewContract?.displayError("Response is null or unsuccessful")
        }
    }

    override fun handleGitHubError() {
        viewContract?.displayLoading(false)
        viewContract?.displayError()
    }
}
