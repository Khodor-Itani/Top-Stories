package com.kdz.topstories

import android.app.Application
import androidx.work.*
import com.kdz.topstories.di.modules.*
import com.kdz.topstories.workmanager.ArticlePollWorker
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named

class TopStoriesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
        launchPollingWorker()
    }

    /**
     * Creates an [ArticlePollWorker], which queries the API as defined in [workerModule].
     */
    private fun launchPollingWorker() {
        val pollRequest: PeriodicWorkRequest by inject(named(POLL_WORK_REQUEST))

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                ArticlePollWorker.TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                pollRequest
            )
    }

    /**
     * Initializes the Dependency Injection library.
     */
    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger()
            androidContext(this@TopStoriesApplication)
            modules(
                listOf(
                    networkingModule,
                    repositoriesModule,
                    cacheModule,
                    viewModelsModule,
                    workerModule,
                    databaseModule,
                    dataSourceModule,
                    storesModule
                )
            )
        }
    }
}