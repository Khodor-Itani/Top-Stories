package com.kdz.topstories.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.kdz.topstories.di.modules.POLL_WORK_REQUEST
import com.kdz.topstories.workmanager.ArticlePollWorker
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class BootBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || !intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) return

        context?.let {
            launchPollingWorker(it)
        }
    }

    private fun launchPollingWorker(context: Context) {
        val pollRequest: PeriodicWorkRequest by inject(named(POLL_WORK_REQUEST))

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                ArticlePollWorker.TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                pollRequest
            )
    }
}