package com.example.bankiapplication.data

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.bankiapplication.data.api.WebViewApi

class WebViewImpl:WebViewApi {
    @SuppressLint("SetJavaScriptEnabled")
    override fun startWebView(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 13; SAMSUNG SM-S911B) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/23.0 Chrome/115.0.0.0 Mobile Safari/537.36"
        webView.loadUrl(START_URL)

    }
    companion object{
        const val START_URL = "http://crapinka.ru/BtGLZhVK?aff_sub4=test"
    }
}