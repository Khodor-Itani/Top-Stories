package com.kdz.topstories

import android.app.Application
import com.kdz.topstories.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TopStoriesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if(BuildConfig.DEBUG) androidLogger()
            androidContext(this@TopStoriesApplication)
            modules(
                listOf(
                    networkingModule,
                    repositoriesModule,
                    storesModule,
                    cacheModule,
                    viewModelsModule
                )
            )
        }

    }
}