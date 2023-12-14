package com.example.bankiapplication.domain

import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient

interface Interactor {
    fun startWebView(webView: WebView, webViewClient:WebViewClient)
    fun getStartUrl():String
    fun checkConnected():Int
    fun loadUrl(webView: WebView)
    fun checkVpn():Boolean
    fun handleIntent(intent: Intent)
}