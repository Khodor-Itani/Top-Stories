package com.kdz.topstories.workmanager

import android.content.Context
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.kdz.topstories.datasources.ArticleDataSource
import com.kdz.topstories.models.Section
import com.kdz.topstories.repositories.ArticlesRepository
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject


/**
 * Periodically polls the API for new articles.
 * Retries if it receives an empty list.
 */

class ArticlePollWorker(context: Context, params: WorkerParameters) : RxWorker(context, params),
    KoinComponent {

    val articleDataSource: ArticleDataSource by inject()

    companion object {
        const val TAG = "ArticlePollWorker"
    }

    override fun createWork(): Single<Result> {
        return articleDataSource.getArticles(Section.HOME).map {
            if(it.isEmpty()) {
                Result.retry()
            } else {
                Result.success()
            }
        }
    }
}