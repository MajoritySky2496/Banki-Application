package com.example.bankiapplication.util

interface NetworkChangeListener {
    fun onNetworkAvailable()
    fun onNetworkUnavailable()
    fun onVpnOn()
}