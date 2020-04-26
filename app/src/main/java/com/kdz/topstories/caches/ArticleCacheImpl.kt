package com.kdz.topstories.caches

import com.kdz.topstories.extensions.toMaybe
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

class ArticleCacheImpl : ArticleCache {

    private val allArticles: MutableMap<Section, List<ArticleEntity>?> by lazy(::initAllArticles)
    private var bookmarkedArticles: List<ArticleEntity>? = null

    private val allObservableArticles: MutableMap<Section, Subject<List<ArticleEntity>?>> by lazy(::initAllObservableArticles)
    private val observableBookmarkedArticles: Subject<List<ArticleEntity>?> by lazy(::initObservableBookmarkedArticles)

    override fun getObservableArticles(section: Section): Observable<List<ArticleEntity>?> {
        return allObservableArticles[section]!!
    }

    override fun setArticles(articles: List<ArticleEntity>, section: Section) {
        allArticles[section] = articles
        allObservableArticles[section]?.onNext(articles)
    }

    override fun getObservableBookmarkedArticles(): Observable<List<ArticleEntity>?> {
        return observableBookmarkedArticles
    }

    override fun getBookmarkedArticles(): Maybe<List<ArticleEntity>?> {
        return bookmarkedArticles.toMaybe()
    }

    override fun setBookmarkedArticles(articles: List<ArticleEntity>) {
        bookmarkedArticles = articles
        observableBookmarkedArticles.onNext(articles)
    }

    override fun getArticles(section: Section): Maybe<List<ArticleEntity>?> {
        return allArticles[section].toMaybe()
    }

    private fun initAllObservableArticles(): MutableMap<Section, Subject<List<ArticleEntity>?>> {
        val map = mutableMapOf<Section, Subject<List<ArticleEntity>?>>()

        Section.values().forEach {
            map[it] = BehaviorSubject.create()
        }

        return map
    }

    private fun initObservableBookmarkedArticles(): Subject<List<ArticleEntity>?> {
        return BehaviorSubject.create()
    }

    private fun initAllArticles(): MutableMap<Section, List<ArticleEntity>?> {
        return mutableMapOf()
    }
}