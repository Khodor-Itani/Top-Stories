package com.kdz.topstories.datasources

import com.kdz.topstories.api.endpoints.NYTimesEndpoint
import com.kdz.topstories.api.responses.GetArticlesResponse
import com.kdz.topstories.caches.ArticleCache
import com.kdz.topstories.models.Article
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import com.kdz.topstories.stores.ArticleStore
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class ArticleDataSourceImpl : ArticleDataSource, KoinComponent {

    val endpoint: NYTimesEndpoint by inject()
    val articleStore: ArticleStore by inject()

    /**
     * Retrieve all [Article]s for the specified section, then push results to [ArticleStore].
     */
    override fun getArticles(section: Section): Single<List<ArticleEntity>?> {
        return endpoint.getTopStories(section.value)
            .map(::mapGetArticlesResponse)
            .map {
                it.map {
                    ArticleEntity.copyFromArticle(it, section)
                }
            }
            .doOnSuccess {
                articleStore.refreshArticleSection(it, section)
            }
            .subscribeOn(Schedulers.io())
    }

    /**
     *  Map the [GetArticlesResponse] into a list of [Article]s.
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