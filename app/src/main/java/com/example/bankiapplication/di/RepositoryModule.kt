package com.example.bankiapplication.di

import com.example.bankiapplication.data.RepositoryImpl
import com.example.bankiapplication.domain.Repository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::RepositoryImpl).bind<Repository>()
}