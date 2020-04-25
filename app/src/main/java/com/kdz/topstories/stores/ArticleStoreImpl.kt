package com.kdz.topstories.stores

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kdz.topstories.models.Article
import com.kdz.topstories.models.Section
import io.reactivex.Observable
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.Collections.emptyList

/**
 * A store built on top of SharedPreferences.
 * It would me more optimal to use a db such as Room or Realm, but I'm using SharedPreferences
 * for the sake of saving time.
 */

class ArticleStoreImpl(val prefs: SharedPreferences) : ArticleStore, KoinComponent {

    companion object {
        val BOOKMARKED_ARTICLES_KEY = "BOOKMARKED_ARTICLES"
    }

    val gson: Gson by inject()

    override fun getArticles(section: Section): Observable<List<Article>> {
        return Observable.create { emitter ->
            val articles = getArticlesInner(section)

            if (!articles.isNullOrEmpty()) {
                emitter.onNext(articles)
            }

            emitter.onComplete()
        }
    }

    /**
     * Persist Article list to SharedPreferences
     */
    override fun setArticles(articles: List<Article>, section: Section) {
        val json = gson.toJson(articles)

        prefs.edit {
            putString(section.value, json)
        }
    }

    override fun getBookmarkedArticles(): Observable<List<Article>> {
        return Observable.create { emitter ->
            val bookmarkedArticles = getBookmarkedArticlesInner()

            if(!bookmarkedArticles.isNullOrEmpty()) {
                emitter.onNext(bookmarkedArticles)
            }

            emitter.onComplete()
        }
    }

    /**
     * Persist Article list to SharedPreferences
     */
    override fun setBookmarkedArticles(articles: List<Article>) {
        val json = gson.toJson(articles)

        prefs.edit {
            putString(BOOKMARKED_ARTICLES_KEY, json)
        }
    }

    /**
     * Get a list of articles for a section from SharedPreferences.
     * Returns an empty list if no articles exist.
     */
    private fun getArticlesInner(section: Section): List<Article> {
        val typeToken = object : TypeToken<List<Article>>() {}.type

        prefs.getString(section.value, null).let {
            return gson.fromJson(it, typeToken) as List<Article>? ?: emptyList()
        }
    }

    /**
     * Get a list of isBookmarked from SharedPreferences.
     * Returns an empty list if no articles exist.
     */
    private fun getBookmarkedArticlesInner(): List<Article> {
        val typeToken = object : TypeToken<List<Article>>() {}.type

        prefs.getString(BOOKMARKED_ARTICLES_KEY, null).let {
            return gson.fromJson(it, typeToken) as List<Article>? ?: emptyList()
        }
    }
}