package com.example.dostoinzaemapp.di

import com.example.dostoinzaemapp.presentation.WebViewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::WebViewViewModel)
}