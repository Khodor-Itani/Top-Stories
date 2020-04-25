package com.kdz.topstories.di.modules

import com.google.gson.GsonBuilder
import com.kdz.topstories.BuildConfig
import com.kdz.topstories.api.API
import com.kdz.topstories.api.endpoints.NYTimesEndpoint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkingModule = module {

    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) addNetworkInterceptor(get<HttpLoggingInterceptor>())
            }
            .build()
    }

    // LoggingInterceptor
    single {
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    // Gson
    single {
        GsonBuilder().create()
    }

    // GsonConverterFactory
    single { GsonConverterFactory.create(get()) as Converter.Factory }

    // RxJavaCallAdapter
    single { RxJava2CallAdapterFactory.create() as CallAdapter.Factory }

    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl(API.baseURL)
            .client(get())
            .addCallAdapterFactory(get())
            .addConverterFactory(get())
            .build()
    }

    // New York Times endpoint. Maybe move to separate module?
    factory { get<Retrofit>().create(NYTimesEndpoint::class.java) }
}