package com.example.bankiapplication.domain

import android.webkit.WebView

class InteractorImpl(private val repository: Repository):Interactor {
    override fun startWebView(webView: WebView) {
        repository.startWebView(webView)
    }
}