package com.kdz.topstories.stores

import com.kdz.topstories.caches.ArticleCache
import com.kdz.topstories.databases.ArticleDatabase
import com.kdz.topstories.extensions.listOrEmpty
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class ArticleStoreImpl : ArticleStore, KoinComponent {

    val articleDatabase: ArticleDatabase by inject()
    val articleCache: ArticleCache by inject()

    init {
        observeAllArticles()
        observeBookmarkedArticles()
    }

    /**
     * Retrieve a [Maybe] that signals whether the database contains a populated list of [ArticleEntity]s for
     * the given [Section].
     */
    override fun getArticles(section: Section): Maybe<List<ArticleEntity>?> {
        return articleDatabase.articleDao()
            .getArticles(section)
            .listOrEmpty()
            .subscribeOn(Schedulers.io())
    }

    /**
     * Persist Article list to storage. Does not overwrite existing [ArticleEntity]s.
     */
    override fun setArticles(articles: List<ArticleEntity>) {
        Single.create<Void> {
            articleDatabase.articleDao().insert(articles)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    /**
     * Returns a [Maybe] that emits a list of bookmarked [ArticleEntity]s, or finishes if the list is null or empty.
     */
    override fun getBookmarkedArticles(): Maybe<List<ArticleEntity>?> {
        return articleDatabase.articleDao()
            .getBookmarked()
            .listOrEmpty()
            .subscribeOn(Schedulers.io())
    }

    /**
     * Persist an [ArticleEntity] to storage.
     */
    override fun updateArticle(article: ArticleEntity) {
        Maybe.create<Boolean> {
            articleDatabase.articleDao().update(article)
            it.onSuccess(true)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    /**
     * Replace non-bookmarked [ArticleEntity]s with new [ArticleEntity]s.
     */
    override fun refreshArticleSection(articles: List<ArticleEntity>, section: Section) {
        deleteAllArticlesKeepBookmarked(section)
            .andThen {
                setArticles(articles)
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    /**
     * Deletes all [ArticleEntity]s, retaining bookmarked [ArticleEntity]s.
     */
    private fun deleteAllArticlesKeepBookmarked(section: Section): Completable {
        return Completable.fromAction {
            articleDatabase.articleDao().deletePreservingBookmarked(section)
        }
            .subscribeOn(Schedulers.io())
    }

    /**
     * Observes the [ArticleDatabase] for changes to the list of all articles. Updates are
     * then pushed into the article [ArticleCache]. Subscribers are created relative to the [ArticleEntity]'s [Section].
     */
    private fun observeAllArticles() {
        Section.values().forEach { section ->

            articleDatabase.articleDao().observeArticles(section).subscribe { articles ->
                articles?.let {
                    articleCache.setArticles(articles, section)
                }
            }

        }
    }

    /**
     * Observes the [ArticleDatabase] for changes to the list of bookmarked [ArticleEntity]s. Updates are
     * then pushed into the [ArticleCache].
     */
    private fun observeBookmarkedArticles() {
        articleDatabase.articleDao().observeBookmarked().subscribe { articles ->
            articles?.let {
                articleCache.setBookmarkedArticles(it)
            }
        }
    }
}