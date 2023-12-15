package com.example.bankiapplication.util.app

import android.app.Application
import com.example.bankiapplication.di.dataModule
import com.example.bankiapplication.di.interactorModule
import com.example.bankiapplication.di.permissionModule
import com.example.bankiapplication.di.repositoryModule
import com.example.bankiapplication.di.viewModelModule
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import com.yandex.metrica.push.YandexMetricaPush
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App:Application() {
    override fun onCreate() {
        super.onCreate()

        val config = YandexMetricaConfig.newConfigBuilder(APP_METRICA_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)
        YandexMetricaPush.init(applicationContext)

        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule, permissionModule)
        }


    }
    companion object{
        const val APP_METRICA_KEY = "e5f43a44-fe39-428e-bbab-47903228fad5"
    }
}
