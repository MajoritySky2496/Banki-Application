package com.example.bankiapplication.data

import android.webkit.WebView
import com.example.bankiapplication.data.api.WebViewApi
import com.example.bankiapplication.domain.Repository

class RepositoryImpl(private val webViewApi: WebViewApi):Repository {
    override fun startWebView(webView: WebView) {
       webViewApi.startWebView(webView)
    }
}