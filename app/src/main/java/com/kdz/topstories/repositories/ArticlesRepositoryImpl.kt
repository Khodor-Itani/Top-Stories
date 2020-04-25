package com.kdz.topstories.repositories

import com.kdz.topstories.api.endpoints.NYTimesEndpoint
import com.kdz.topstories.api.responses.GetArticlesResponse
import com.kdz.topstories.caches.ArticleCache
import com.kdz.topstories.models.Article
import com.kdz.topstories.models.Section
import com.kdz.topstories.stores.ArticleStore
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/*
 * Manages
 */
class ArticlesRepositoryImpl(
    val endpoint: NYTimesEndpoint,
    val articleStore: ArticleStore,
    val articleCache: ArticleCache
) : ArticlesRepository {

    /**
     * Returns the requested Top stories for the section.
     * Prioritizes fetching from Caches and Stores before calling the API.
     */
    override fun getTopStories(section: Section): Observable<List<Article>> {
        val fromCache = articleCache.getArticles(section)
        val fromStore = articleStore.getArticles(section)
            .doOnNext {
                articleCache.setArticles(it, section)
            }
        val fromNetwork = endpoint.getTopStories(section.value)
            .map(::mapGetArticlesResponse).doOnNext {
                articleCache.setArticles(it, section)
                articleStore.setArticles(it, section)
            }

        // TODO: Handle edge case of all three requests failing.

        return Observable.concat(fromCache, fromStore, fromNetwork)
            .firstElement()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
    }

    /**
     * Retrieves isBookmarked articles, prioritizing reading from Cache over Storage.
     */
    override fun getBookmarkedArticles(): Observable<List<Article>> {
        val fromCache = articleCache.getBookmarkedArticles()
        val fromStore = articleStore.getBookmarkedArticles()
            .doOnNext {
                articleCache.setBookmarkedArticles(it)
            }

        return Observable.concat(fromCache, fromStore)
            .firstElement()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
    }

    /**
     * Called periodically in order to poll the API for Article updates.
     */
    override fun pollTopStories() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     *  Map the articles response into a list of articles.
     */
    private fun mapGetArticlesResponse(response: GetArticlesResponse): List<Article> {

        if (response.isSuccess()) {
            response.results?.let {
                return it
            }
        }

        response.status?.let {
            throw Exception(it)
        }

        throw Exception("GetArticles failed with no status")
    }
}