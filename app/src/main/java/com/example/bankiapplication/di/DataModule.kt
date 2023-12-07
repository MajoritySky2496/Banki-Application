package com.example.bankiapplication.di

import com.example.bankiapplication.data.WebViewImpl
import com.example.bankiapplication.data.api.WebViewApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module{
    singleOf(::WebViewImpl).bind<WebViewApi>()
}