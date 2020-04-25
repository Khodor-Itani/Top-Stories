package com.kdz.topstories.di.modules

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import com.kdz.topstories.workmanager.ArticlePollWorker
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

const val CONNECTIVITY_AWARE_WORKER_CONSTRAINT = "CONNECTIVITY_AWARE_WORKER_CONSTRAINT"
const val POLL_WORK_REQUEST = "POLL_WORK_REQUEST"

val workerModule = module {
    // Work Request constraints requiring network connectivity

    factory(named(CONNECTIVITY_AWARE_WORKER_CONSTRAINT)) {
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    // Default Article-polling Work Request
    factory(named(POLL_WORK_REQUEST)) {
        PeriodicWorkRequestBuilder<ArticlePollWorker>(12, TimeUnit.HOURS)
            .setConstraints(get(named(CONNECTIVITY_AWARE_WORKER_CONSTRAINT)))
            .build()
    }
}