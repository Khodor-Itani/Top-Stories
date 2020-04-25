package com.kdz.topstories.di.modules

import com.kdz.topstories.ui.main.BookmarksViewModel
import com.kdz.topstories.ui.main.TopStoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { TopStoriesViewModel() }
    viewModel { BookmarksViewModel() }
}