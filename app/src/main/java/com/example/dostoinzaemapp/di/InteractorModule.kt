package com.example.dostoinzaemapp.di

import com.example.dostoinzaemapp.domain.Interactor
import com.example.dostoinzaemapp.domain.InteractorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val interactorModule = module {
    singleOf(::InteractorImpl).bind<Interactor>()
}