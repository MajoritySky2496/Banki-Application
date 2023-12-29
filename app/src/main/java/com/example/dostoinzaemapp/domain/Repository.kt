package com.example.dostoinzaemapp.domain

import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient

interface Repository {
    fun startWebView(webView: WebView, webViewClient: WebViewClient)
    fun getStartUrl():String
    fun checkConnection():Boolean
    fun loadUrl(webView: WebView)
    fun checkVpn():Boolean
    fun handleIntent(intent: Intent)
}