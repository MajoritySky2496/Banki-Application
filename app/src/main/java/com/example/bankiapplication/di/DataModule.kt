package com.example.bankiapplication.di

import android.content.Context
import com.example.bankiapplication.data.DeepLinkStorageImpl
import com.example.bankiapplication.data.DeepLinkStorageImpl.Companion.FALSE
import com.example.bankiapplication.data.UniqueLinkStorageImpl
import com.example.bankiapplication.data.WebViewImpl
import com.example.bankiapplication.data.api.WebViewApi
import com.example.bankiapplication.data.localStorage.DeepLinkStorage
import com.example.bankiapplication.data.localStorage.UniqueLinkStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module{
    singleOf(::WebViewImpl).bind<WebViewApi>()
    single {
        androidContext()
            .getSharedPreferences(
                FALSE, Context.MODE_PRIVATE
            )
    }
    singleOf(::DeepLinkStorageImpl).bind<DeepLinkStorage>()
    singleOf(::UniqueLinkStorageImpl).bind<UniqueLinkStorage>()

}