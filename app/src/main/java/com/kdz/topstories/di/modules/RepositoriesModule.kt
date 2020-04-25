package com.kdz.topstories.di.modules

import com.kdz.topstories.repositories.ArticlesRepository
import com.kdz.topstories.repositories.ArticlesRepositoryImpl
import org.koin.dsl.module

val repositoriesModule = module {
    single { ArticlesRepositoryImpl(get(), get(), get()) as ArticlesRepository }
}
