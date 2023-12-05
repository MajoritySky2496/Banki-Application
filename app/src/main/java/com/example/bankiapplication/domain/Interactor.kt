package com.example.bankiapplication.domain

import android.webkit.WebView
import android.webkit.WebViewClient

interface Interactor {
    fun startWebView(webView: WebView, webViewClient:WebViewClient)
}