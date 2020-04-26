package com.kdz.topstories.caches

import com.kdz.topstories.extensions.toMaybe
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

/**
 * Caches lists of [ArticleEntity]s. Each article [Subject] has a backing [List] associated with it.
 * Article subjects will receive updates from the ArticleStore.
 */
class ArticleCacheImpl : ArticleCache {

    private val allArticles: MutableMap<Section, List<ArticleEntity>?> by lazy(::initAllArticles)
    private var bookmarkedArticles: List<ArticleEntity>? = null

    private val allObservableArticles: MutableMap<Section, Subject<List<ArticleEntity>?>> by lazy(::initAllObservableArticles)
    private val observableBookmarkedArticles: Subject<List<ArticleEntity>?> by lazy(::initObservableBookmarkedArticles)

    /**
     * @return an observable list of [ArticleEntity]s for a certain section.
     */
    override fun getObservableArticles(section: Section): Observable<List<ArticleEntity>?> {
        return allObservableArticles[section]!!
    }

    /**
     * Sets the backing field for a list of [ArticleEntity]s, and notifies the respective [Subject]
     */
    override fun setArticles(articles: List<ArticleEntity>, section: Section) {
        allArticles[section] = articles
        allObservableArticles[section]?.onNext(articles)
    }

    /**
     * @return an observable list of bookmarked [ArticleEntity]s.
     */
    override fun getObservableBookmarkedArticles(): Observable<List<ArticleEntity>?> {
        return observableBookmarkedArticles
    }

    /**
     * @return a [Maybe] that emits a list of bookmarked [ArticleEntity], or finishes if the [List] is null or empty.
     */
    override fun getBookmarkedArticles(): Maybe<List<ArticleEntity>?> {
        return bookmarkedArticles.toMaybe()
    }

    /**
     * Sets the backing field for a list of bookmarked [ArticleEntity]s, and notifies the respective [Subject]
     */
    override fun setBookmarkedArticles(articles: List<ArticleEntity>) {
        bookmarkedArticles = articles
        observableBookmarkedArticles.onNext(articles)
    }

    /**
     * @return a [Maybe] that emits a list of [ArticleEntity] for a section, or finishes if the [List] is null or empty.
     */
    override fun getArticles(section: Section): Maybe<List<ArticleEntity>?> {
        return allArticles[section].toMaybe()
    }

    /**
     * @return a [MutableMap] of empty [BehaviorSubject]s.
     */
    private fun initAllObservableArticles(): MutableMap<Section, Subject<List<ArticleEntity>?>> {
        val map = mutableMapOf<Section, Subject<List<ArticleEntity>?>>()

        Section.values().forEach {
            map[it] = BehaviorSubject.create()
        }

        return map
    }

    /**
     * @return an empty [BehaviorSubject].
     */
    private fun initObservableBookmarkedArticles(): Subject<List<ArticleEntity>?> {
        return BehaviorSubject.create()
    }

    /**
     * @return a [MutableMap] of lists.
     */
    private fun initAllArticles(): MutableMap<Section, List<ArticleEntity>?> {
        return mutableMapOf()
    }
}