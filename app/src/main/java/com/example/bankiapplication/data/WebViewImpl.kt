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
        webView.loadUrl(START_URL)

    }
    companion object{
        const val START_URL = "https://crapinka.ru/BtGLZhVK?aff_sub4=test"
    }
}