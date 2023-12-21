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
import androidx.core.net.toUri
import com.example.bankiapplication.data.api.WebViewApi
import com.example.bankiapplication.data.localStorage.DeepLinkStorage
import com.example.bankiapplication.util.webview.MyWebChromeClient
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class WebViewImpl(private val context: Context, private val deepLinkStorage: DeepLinkStorage) : WebViewApi {

    private var startUrl = "https://crapinka.ru/BtGLZhVK?aff_sub1=test.zaim.german&aff_sub2=%7Bdeep_adv%7D&aff_sub3=%7Bdeep_place%7D&aff_sub4=boy_showcase&aff_sub5=%7Bdevice_id%7D"
    private var startUrlVpn = "https://crapinka.ru/BtGLZhVK?aff_sub1=test.zaim.german&aff_sub2={deep_adv}&aff_sub3={deep_place}&aff_sub4=vpn&aff_sub5={device_id}"
    private val deepLinkList = mutableListOf<String?>()
    private val newDeepLinkList = mutableListOf<String?>(null, null)
    private var advId:String? = null


    @SuppressLint("SetJavaScriptEnabled")
    override fun startWebView(webView: WebView, webViewClient: WebViewClient) {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.setGeolocationEnabled(true)
        webView.isHorizontalScrollBarEnabled = false
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
        getNewUrlParameters(appLinkData)
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
            false -> return addDeeplink( startUrl)
            true -> return addDeeplink( startUrlVpn)
        }
    }
    private fun getNewUrlParameters(appLinkData:Uri?){
        Log.d("appLinksData", "$appLinkData")
        val deeplink = appLinkData?.getQueryParameter("aff_sub2")
        Log.d("appLinksData", "$deeplink")
        val deeplink2 = appLinkData?.getQueryParameter("aff_sub3")
        Log.d("appLinksData", "$deeplink2")
        val deepLinkList = mutableListOf<String>()
        deeplink?.let { deepLinkList.add(it) }
        deeplink2?.let { deepLinkList.add(it) }
        this.deepLinkList.clear()
        this.newDeepLinkList.addAll(deepLinkList)
        saveDeepLinks(deepLinkList)
        Log.d("appLinksData", "$deepLinkList")
    }
    private fun getUrlParameters(url:String){
        val uri = url.toUri()
        val deepLink1 = uri.getQueryParameter("aff_sub1")
        val deepLink2 = uri.getQueryParameter("aff_sub2")
        val deepLink3 = uri.getQueryParameter("aff_sub3")
        val deepLink4 = uri.getQueryParameter("aff_sub4")
        val deepLink5 = uri.getQueryParameter("aff_sub5")
        val deepLinkList = listOf(deepLink1, deepLink2, deepLink3, deepLink4, deepLink5)
        this.deepLinkList.addAll(deepLinkList)

    }
    private fun addDeeplink(url:String):String{
        if(getDeepLinkFromStorage().isNotEmpty()) {
            newDeepLinkList.clear()
            newDeepLinkList.addAll(getDeepLinkFromStorage())
        }
        deepLinkList.clear()
        getUrlParameters(url)
        val updatedUrl = Uri.parse(url)
            .buildUpon()
            .clearQuery()
            .appendQueryParameter("aff_sub1", deepLinkList.get(0))
            .appendQueryParameter("aff_sub2", checkDeepLinkNull(deepLinkList.get(1)!!, newDeepLinkList.get(0)))
            .appendQueryParameter("aff_sub3", checkDeepLinkNull(deepLinkList.get(2)!!, newDeepLinkList.get(1)))
            .appendQueryParameter("aff_sub4", deepLinkList.get(3))
            .appendQueryParameter("aff_sub5", advId)
            .build()
        Log.d("urlStartNew", updatedUrl.toString())
        return updatedUrl.toString()
    }
    private fun checkDeepLinkNull(deepLink:String, newDeepLink:String?):String{
        if(newDeepLink!=null){
            return newDeepLink
        }else{
            return deepLink
        }
    }
    private fun saveDeepLinks(deeplinkList:List<String>){
        if(getDeepLinkFromStorage().isEmpty()) {
            deepLinkStorage.doWrite(deeplinkList)
        }
    }
    private fun getDeepLinkFromStorage():List<String>{
       val deepLinksFromStorage =  deepLinkStorage.doRequest()
        return deepLinksFromStorage.toList()
    }
//    fun getAdvertisingId(context: Context) {
//        GlobalScope.launch {
//
//            try {
//                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
//                val advertisingId = adInfo.id
//                advId = advertisingId
//                Log.d("avdId", "$advId")
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

}