package com.example.bankiapplication.data.api

import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient

interface WebViewApi {
    fun startWebView(webView: WebView, webViewClient: WebViewClient)
    fun getStartUrl():String
    fun isConnected():Boolean
    fun loadUrl(webView: WebView)
    fun checkVpn():Boolean
    fun handleIntent(intent: Intent)
}