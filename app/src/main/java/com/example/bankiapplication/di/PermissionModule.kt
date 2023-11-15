package com.example.bankiapplication.di

import com.example.bankiapplication.util.CheckPermissions
import org.koin.dsl.module

val permissionModule = module {

    single {CheckPermissions(get())}
}