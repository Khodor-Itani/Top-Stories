package com.kdz.topstories.caches

import com.kdz.topstories.models.Article
import com.kdz.topstories.models.Section
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject

class ArticleCacheImpl : ArticleCache {

    private val _allArticles: MutableMap<Section, List<Article>> by lazy(::initAllArticles)
    private var _bookmarkedArticles: List<Article> = emptyList()

    override fun getArticles(section: Section): Observable<List<Article>> {
        return Observable.create { emitter ->
            val articlesInSection = _allArticles[section]

            if (!articlesInSection.isNullOrEmpty()) {
                emitter.onNext(articlesInSection)
            }

            emitter.onComplete()
        }
    }

    override fun setArticles(articles: List<Article>, section: Section) {
        _allArticles[section] = articles
    }

    override fun getBookmarkedArticles(): Observable<List<Article>> {
        return Observable.create { emitter ->
            if (!_bookmarkedArticles.isNullOrEmpty()) {
                emitter.onNext(_bookmarkedArticles)
            }

            emitter.onComplete()
        }
    }

    override fun setBookmarkedArticles(articles: List<Article>) {
        _bookmarkedArticles = articles
    }

    private fun initAllArticles(): MutableMap<Section, List<Article>> {
        val map = mutableMapOf<Section, List<Article>>()

        Section.values().forEach {
            map[it] = emptyList()
        }

        return map
    }
}