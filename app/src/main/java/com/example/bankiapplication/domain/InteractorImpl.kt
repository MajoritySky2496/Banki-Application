package com.example.bankiapplication.domain

import android.webkit.WebView
import android.webkit.WebViewClient

class InteractorImpl(private val repository: Repository):Interactor {
    override fun startWebView(webView: WebView, webViewClient: WebViewClient) {
        repository.startWebView(webView, webViewClient)
    }

    override fun getStartUrl(): String {
       return repository.getStartUrl()
    }

    override fun checkConnected(): Int {
        val internetSatus = repository.checkConnection()
        if(internetSatus!=true){
            return 500
        }else{
            return 200
        }
    }
}