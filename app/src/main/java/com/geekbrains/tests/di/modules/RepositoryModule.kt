package com.geekbrains.tests.di.modules

import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.repository.FakeGitHubRepository
import com.geekbrains.tests.repository.GitHubApi
import com.geekbrains.tests.repository.GitHubRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.github.com"

internal val repositoryModule = module {

    single<GitHubApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApi::class.java)
    }

    factory<RepositoryContract>(named("real")) {
        GitHubRepository(gitHubApi = get())
    }

    factory<RepositoryContract>(named("fake")) {
        FakeGitHubRepository()
    }
}

