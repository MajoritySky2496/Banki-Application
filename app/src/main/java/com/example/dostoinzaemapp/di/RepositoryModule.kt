package com.example.dostoinzaemapp.di

import com.example.dostoinzaemapp.data.RepositoryImpl
import com.example.dostoinzaemapp.domain.Repository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::RepositoryImpl).bind<Repository>()
}