package com.kdz.topstories.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kdz.topstories.models.Article
import com.kdz.topstories.models.Section
import com.kdz.topstories.repositories.ArticlesRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.koin.core.KoinComponent
import org.koin.core.inject

class BookmarksViewModel : ViewModel(), KoinComponent {

    val articlesRepository: ArticlesRepository by inject()
    val compositeDisposable = CompositeDisposable()

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>>
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

    fun removeBookmark(article: Article) {

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}