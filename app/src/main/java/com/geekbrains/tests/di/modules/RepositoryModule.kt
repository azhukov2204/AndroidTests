package com.geekbrains.tests.di.modules

import com.geekbrains.tests.repository.RepositoryContract
import com.geekbrains.tests.repository.FakeGitHubRepository
import com.geekbrains.tests.repository.GitHubApi
import com.geekbrains.tests.repository.GitHubRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.github.com"

internal val repositoryModule = module {

    single<GitHubApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
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

