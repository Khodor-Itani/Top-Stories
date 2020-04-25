package com.kdz.topstories.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.kdz.topstories.BuildConfig
import com.kdz.topstories.api.API
import com.kdz.topstories.api.endpoints.NYTimesEndpoint
import com.kdz.topstories.caches.ArticleCache
import com.kdz.topstories.caches.ArticleCacheImpl
import com.kdz.topstories.repositories.ArticlesRepository
import com.kdz.topstories.repositories.ArticlesRepositoryImpl
import com.kdz.topstories.stores.ArticleStore
import com.kdz.topstories.stores.ArticleStoreImpl
import com.kdz.topstories.ui.main.TopStoriesViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val ARTICLE_STORAGE_KEY = "ARTICLE_STORAGE_KEY"

val networkingModule = module {

    single {
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) addNetworkInterceptor(get<HttpLoggingInterceptor>())
            }
            .build()
    }

    single {
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    single {
        GsonBuilder().create()
    }

    single { GsonConverterFactory.create(get()) as Converter.Factory }

    single { RxJava2CallAdapterFactory.create() as CallAdapter.Factory }

    single {
        Retrofit.Builder()
            .baseUrl(API.baseURL)
            .client(get())
            .addCallAdapterFactory(get())
            .addConverterFactory(get())
            .build()
    }

    factory { get<Retrofit>().create(NYTimesEndpoint::class.java) }
}

val viewModelsModule = module {
    viewModel { TopStoriesViewModel() }
}

val repositoriesModule = module {
    single { ArticlesRepositoryImpl(get(), get(), get()) as ArticlesRepository }
}

val storesModule = module {
    single { ArticleStoreImpl(get(named(ARTICLE_STORAGE_KEY))) as ArticleStore }

    single(named(ARTICLE_STORAGE_KEY)) { provideSharedPreferences(get(), ARTICLE_STORAGE_KEY) }
}

val cacheModule = module {
    single { ArticleCacheImpl() as ArticleCache }
}

fun provideSharedPreferences(context: Context, name: String): SharedPreferences {
    return context.getSharedPreferences(name, Context.MODE_PRIVATE)
}
