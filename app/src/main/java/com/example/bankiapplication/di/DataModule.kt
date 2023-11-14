package com.example.bankiapplication.di

import com.example.bankiapplication.data.WebViewImpl
import com.example.bankiapplication.data.api.WebViewApi
import org.koin.dsl.module

val dataModule = module{
    single<WebViewApi> {WebViewImpl()  }
}