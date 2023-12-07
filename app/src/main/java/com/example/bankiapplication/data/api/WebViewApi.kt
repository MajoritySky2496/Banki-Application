package com.example.bankiapplication.data.api

import android.webkit.WebView
import android.webkit.WebViewClient

interface WebViewApi {
    fun startWebView(webView: WebView, webViewClient: WebViewClient)
    fun getStartUrl():String
}