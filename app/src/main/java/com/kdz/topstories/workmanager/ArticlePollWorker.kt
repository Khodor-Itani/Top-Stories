package com.kdz.topstories.workmanager

import android.content.Context
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.kdz.topstories.models.Section
import com.kdz.topstories.repositories.ArticlesRepository
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject


/**
 * Periodically polls the API for new articles.
 *
 * Considerations: Not sure if it's a good practice to be referencing the entire repo here.
 * Maybe just reference the Store directly instead?
 */

class ArticlePollWorker(context: Context, params: WorkerParameters) : RxWorker(context, params),
    KoinComponent {

    val articlesRepository: ArticlesRepository by inject()

    companion object {
        const val TAG = "ArticlePollWorker"
    }

    override fun createWork(): Single<Result> {
        return articlesRepository
            .pollTopStories(Section.HOME)
            .map {
                Result.success()
            }
    }
}