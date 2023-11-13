package com.example.bankiapplication.di

import com.example.bankiapplication.domain.Interactor
import com.example.bankiapplication.domain.InteractorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val interactorModule = module {
    singleOf(::InteractorImpl).bind<Interactor>()
}