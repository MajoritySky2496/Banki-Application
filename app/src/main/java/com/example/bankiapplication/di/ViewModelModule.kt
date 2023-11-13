package com.example.bankiapplication.di

import com.example.bankiapplication.presentation.WebViewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::WebViewViewModel)
}