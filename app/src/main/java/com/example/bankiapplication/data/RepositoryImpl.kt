package com.example.bankiapplication.data

import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.bankiapplication.data.api.WebViewApi
import com.example.bankiapplication.domain.Repository

class RepositoryImpl(private val webViewApi: WebViewApi):Repository {
    override fun startWebView(webView: WebView, webViewClient: WebViewClient) {
       webViewApi.startWebView(webView, webViewClient)
    }

    override fun getStartUrl(): String {
        return webViewApi.getStartUrl()
    }
}