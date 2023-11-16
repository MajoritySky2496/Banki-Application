package com.example.bankiapplication.data

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.example.bankiapplication.data.api.WebViewApi
import com.example.bankiapplication.util.MyWebChromeClient

class WebViewImpl:WebViewApi {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    override fun startWebView(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowContentAccess =true
        webView.settings.setGeolocationEnabled(true)
        webView.settings.allowFileAccess =true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = MyWebChromeClient(webView.context as Activity)


        webView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 13; SAMSUNG SM-S911B) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/23.0 Chrome/115.0.0.0 Mobile Safari/537.36"
        webView.loadUrl(START_URL)
    }
    companion object{
        const val START_URL = "https://crapinka.ru/BtGLZhVK?aff_sub4=test"
    }
}