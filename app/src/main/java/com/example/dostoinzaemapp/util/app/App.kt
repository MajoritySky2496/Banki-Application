package com.example.dostoinzaemapp.util.app

import android.app.Application
import com.example.dostoinzaemapp.di.dataModule
import com.example.dostoinzaemapp.di.interactorModule
import com.example.dostoinzaemapp.di.permissionModule
import com.example.dostoinzaemapp.di.repositoryModule
import com.example.dostoinzaemapp.di.viewModelModule
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
        const val APP_METRICA_KEY = "039fc67b-5057-496c-a054-a8b8061a7749"
    }
}
