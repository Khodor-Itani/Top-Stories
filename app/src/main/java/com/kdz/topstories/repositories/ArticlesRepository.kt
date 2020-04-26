package com.kdz.topstories.repositories

import com.kdz.topstories.models.Article
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import io.reactivex.Observable
import io.reactivex.Single

interface ArticlesRepository {
    fun getTopStories(section: Section): Observable<List<ArticleEntity>?>

    fun getBookmarkedArticles(): Observable<List<ArticleEntity>?>

    fun pollTopStories(section: Section): Single<List<ArticleEntity>?>

    fun bookmarkArticle(article: ArticleEntity, bookmarked: Boolean)
}