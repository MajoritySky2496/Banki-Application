package com.example.bankiapplication.data

import android.content.Intent
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

    override fun checkConnection(): Boolean {
        return webViewApi.isConnected()
    }

    override fun loadUrl(webView: WebView) {
        webViewApi.loadUrl(webView)
    }

    override fun checkVpn():Boolean {
        return webViewApi.checkVpn()
    }

    override fun handleIntent(intent: Intent) {
        webViewApi.handleIntent(intent)
    }
}