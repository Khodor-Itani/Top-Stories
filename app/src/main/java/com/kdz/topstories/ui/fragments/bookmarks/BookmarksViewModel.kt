package com.kdz.topstories.ui.fragments.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kdz.topstories.models.Article
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import com.kdz.topstories.repositories.ArticlesRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.koin.core.KoinComponent
import org.koin.core.inject

class BookmarksViewModel : ViewModel(), KoinComponent {

    private val articlesRepository: ArticlesRepository by inject()
    private val compositeDisposable = CompositeDisposable()

    private val _articles = MutableLiveData<List<ArticleEntity>>()
    val articles: LiveData<List<ArticleEntity>>
        get() = _articles

    init {
        getArticles()
    }

    private fun getArticles() {
        articlesRepository.getBookmarkedArticles().subscribe({
            _articles.postValue(it)
        }, {
            //TODO: Handle failure
        }).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}