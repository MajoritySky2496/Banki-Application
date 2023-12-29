package com.example.dostoinzaemapp.di

import com.example.dostoinzaemapp.util.CheckPermissions
import org.koin.dsl.module

val permissionModule = module {

    single {CheckPermissions(get())}
}