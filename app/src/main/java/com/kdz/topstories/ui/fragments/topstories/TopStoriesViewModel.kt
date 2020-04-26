package com.kdz.topstories.ui.fragments.topstories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kdz.topstories.R
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import com.kdz.topstories.repositories.ArticlesRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.koin.core.KoinComponent
import org.koin.core.inject

// TODO: Use LiveData to update bookmark image color
//  instead of relying on list updates from repo and DiffUtil.

/**
 * Provides Article Data and bookmarking functionalities for the subscribed view.
 */

class TopStoriesViewModel : ViewModel(), KoinComponent {

    private val articlesRepository: ArticlesRepository by inject()
    private val compositeDisposable = CompositeDisposable()

    private val _snackBar = MutableLiveData<Int>()
    val snackbar: LiveData<Int>
    get() = _snackBar

    private val _articles = MutableLiveData<List<ArticleEntity>>()
    val articles: LiveData<List<ArticleEntity>>
        get() = _articles

    init {
        getArticles()
    }

    private fun getArticles() {
        articlesRepository.getTopStories(Section.HOME).subscribe({
            _articles.postValue(it)
        }, {
            //TODO: Handle failure
        }).addTo(compositeDisposable)
    }

    fun toggleArticleBookmark(article: ArticleEntity) {
        val targetBookmark = !article.isBookmarked

        val prompt = if(targetBookmark) {
            R.string.added_bookmark
        } else {
            R.string.removed_bookmark
        }

        _snackBar.postValue(prompt)

        articlesRepository.bookmarkArticle(article, targetBookmark)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}