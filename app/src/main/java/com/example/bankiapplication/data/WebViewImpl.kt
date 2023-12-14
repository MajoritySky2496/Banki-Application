package com.example.bankiapplication.data

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.bankiapplication.data.api.WebViewApi
import com.example.bankiapplication.util.webview.MyWebChromeClient

class WebViewImpl(private val context: Context) : WebViewApi {

    private var startUrl = "http://crapinka.ru/BtGLZhVK?aff_sub4=test"
    private var startUrlVpn = "http://crapinka.ru/BtGLZhVK?aff_sub4=vpn"
    private val deepLinkList = mutableListOf<String?>()


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
    }


    override fun checkVpn(): Boolean {

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

    override fun handleIntent(intent: Intent){
        val appLinkData: Uri? = intent.data
        getUrlLink(appLinkData)
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

    override fun loadUrl(webView: WebView) {
        Log.d("load", getStartUrl() )
        webView.loadUrl(getStartUrl())

    }


    override fun getStartUrl(): String {
        when (checkVpn()) {
            false -> return addDeeplink(deepLinkList, startUrl)
            true -> return addDeeplink(deepLinkList, startUrlVpn)
        }

    }
    private fun getUrlLink(appLinkData:Uri?){
        Log.d("appLinksData", "$appLinkData")
        val deeplink = appLinkData?.getQueryParameter("aff_sub2")
        Log.d("appLinksData", "$deeplink")
        val deeplink2 = appLinkData?.getQueryParameter("aff_sub3")
        Log.d("appLinksData", "$deeplink2")
        val deepLinkList = listOf(deeplink, deeplink2)
        this.deepLinkList.addAll(deepLinkList)
        Log.d("appLinksData", "$deepLinkList")
    }
    private fun addDeeplink(deepLinkList:List<String?>, url:String):String{
        val startUrlList = url.split("?")
        val starUrldeeplink = startUrlList.get(1)
        val host = startUrlList.get(0)
        val urlStartNew = host+"?"+"aff_sub2="+deepLinkList.get(0)+"&"+"aff_sub3="+deepLinkList.get(1)+"&"+starUrldeeplink
        Log.d("urlStartNew", urlStartNew)
        return urlStartNew

    }
}