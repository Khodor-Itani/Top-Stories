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

    // Provide Work Request constraints that require network activity

    factory(named(CONNECTIVITY_AWARE_WORKER_CONSTRAINT)) {
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    // Default Article-polling Work Request.
    // Setting poll time to 15 minutes for debug purposes.

    factory(named(POLL_WORK_REQUEST)) {
        PeriodicWorkRequestBuilder<ArticlePollWorker>(15, TimeUnit.MINUTES)
            .setConstraints(get(named(CONNECTIVITY_AWARE_WORKER_CONSTRAINT)))
            .build()
    }
}