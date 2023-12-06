package com.example.bankiapplication.domain

import android.webkit.WebView
import android.webkit.WebViewClient

interface Repository {
    fun startWebView(webView: WebView, webViewClient: WebViewClient)

}