package com.example.dostoinzaemapp.di

import android.content.Context
import com.example.dostoinzaemapp.data.DeepLinkStorageImpl
import com.example.dostoinzaemapp.data.DeepLinkStorageImpl.Companion.FALSE
import com.example.dostoinzaemapp.data.UniqueLinkStorageImpl
import com.example.dostoinzaemapp.data.WebViewImpl
import com.example.dostoinzaemapp.data.api.WebViewApi
import com.example.dostoinzaemapp.data.localStorage.DeepLinkStorage
import com.example.dostoinzaemapp.data.localStorage.UniqueLinkStorage
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