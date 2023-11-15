package com.example.bankiapplication.util.app

import android.app.Application
import com.example.bankiapplication.di.dataModule
import com.example.bankiapplication.di.interactorModule
import com.example.bankiapplication.di.permissionModule
import com.example.bankiapplication.di.repositoryModule
import com.example.bankiapplication.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule, permissionModule)
        }
    }
}