package com.kdz.topstories.repositories

import com.kdz.topstories.caches.ArticleCache
import com.kdz.topstories.datasources.ArticleDataSource
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import com.kdz.topstories.stores.ArticleStore
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/*
 * Implements CRUD operations and business logic for bookmarked and non-bookmarked articles.
 */

// TODO: Move to a Coroutines implementation if I have enough time

class ArticlesRepositoryImpl(
    val articleDataSource: ArticleDataSource,
    val articleStore: ArticleStore,
    val articleCache: ArticleCache
) : ArticlesRepository {

    var networkDisposable: Disposable? = null

    /**
     * Returns the requested Top stories for the section.
     * Prioritizes fetching from Caches and Stores before calling the API.
     */
    override fun getTopStories(section: Section): Observable<List<ArticleEntity>?> {
        // If we're not doing any work, query the stream for data.

        val cache = articleCache.getArticles(section).toObservable()
        val store = articleStore.getArticles(section).toObservable()
        val network = articleDataSource.getArticles(section).toObservable()

        if (!isCallingNetwork()) {
            networkDisposable =
                Observable.concat(cache, store, network)
                    .firstElement()
                    .subscribeOn(Schedulers.io())
                    .subscribe()
        }

        return articleCache.getObservableArticles(section)
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Retrieves bookmarked [ArticleEntity]s, prioritizing reading from Cache over Storage.
     */
    override fun getBookmarkedArticles(): Observable<List<ArticleEntity>?> {
        val cache = articleCache.getBookmarkedArticles().toObservable()
        val store = articleStore.getBookmarkedArticles().toObservable()

        Observable.concat(cache, store)
            .first(emptyList())
            .subscribeOn(Schedulers.io())
            .subscribe()

        return articleCache.getObservableBookmarkedArticles()
    }

    /**
     * Called periodically in order to poll the API for [ArticleEntity] updates.
     */
    override fun pollTopStories(section: Section): Single<List<ArticleEntity>?> {
        return articleDataSource.getArticles(section)
    }

    /**
     * Changes the [ArticleEntity.isBookmarked] flag and updates the store.
     */
    override fun bookmarkArticle(article: ArticleEntity, bookmarked: Boolean) {
        article.copy().also {
            it.isBookmarked = bookmarked
            articleStore.updateArticle(it)
        }
    }

    /**
     * Checks if network requests are being made, since we might have multiple sources
     * requesting articles.
     */
    private fun isCallingNetwork(): Boolean {
        return networkDisposable?.isDisposed?.not() ?: false
    }
}