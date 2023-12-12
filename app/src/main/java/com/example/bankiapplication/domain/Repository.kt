package com.example.bankiapplication.domain

import android.webkit.WebView
import android.webkit.WebViewClient

interface Repository {
    fun startWebView(webView: WebView, webViewClient: WebViewClient)
    fun getStartUrl():String
    fun checkConnection():Boolean
    fun loadUrl(webView: WebView)
    fun checkVpn():Boolean
}