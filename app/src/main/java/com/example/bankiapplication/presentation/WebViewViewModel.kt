package com.example.bankiapplication.presentation

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankiapplication.data.localStorage.UniqueLinkStorage
import com.example.bankiapplication.domain.Interactor
import com.example.bankiapplication.ui.model.WebViewFragmentState
import com.example.bankiapplication.util.CheckPermissions
import com.example.bankiapplication.util.app.App
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.yandex.metrica.YandexMetrica
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WebViewViewModel(
    private val interactor: Interactor,
    private val checkPermissions: CheckPermissions,
    private val uniqueLinkStorage: UniqueLinkStorage,
    context: Context
) : ViewModel() {
    lateinit var webView: WebView
    private var uniqueLink: String? = null
    private var urlList = mutableListOf<String>()
    private var startUrl: String? = null
    private var startUrlVpn: String? = null
    private var currentUrl:String? = null
    var currentVpnStatus = true

    private val vpnStateReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                context?.let {
                    checkVpn()
                }
            }
        }

    }
    private val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _viewStateLiveData.postValue(WebViewFragmentState.InternetAvailable)
            Log.d("checkInternet", "OK")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d("checkInternet", "Error")
            _viewStateLiveData.postValue(WebViewFragmentState.NoConnection)

        }
    }
    private val webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showView(url!!, getStarUrl(url!!) )
            currentUrl = url!!
            Log.d("myLog", "start")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (uniqueLink != null) {
                loadUniqueLink()

            }
            Log.d("myLog", "finish")
        }
    }

    init {
        getToken()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(context, vpnStateReceiver, intentFilter, RECEIVER_EXPORTED)

    }

    private var _viewStateLiveData = MutableLiveData<WebViewFragmentState>()
    val viewStateLiveData: LiveData<WebViewFragmentState> = _viewStateLiveData


    fun showWebView(webView: WebView) {
        this.webView = webView
        when (interactor.checkConnected()) {
            500 -> {
                _viewStateLiveData.postValue(WebViewFragmentState.NoConnection)
                interactor.startWebView(webView, webViewClient)
            }

            200 -> interactor.startWebView(webView, webViewClient)
        }
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("MyLog", token)
        })
    }

    fun webViewGoBack(webView: WebView) {
        webView.goBack()
    }

    fun finish(webView: WebView) {
        if (currentUrl!=getStarUrl(currentUrl!!)) {
            webView.goBack()
        } else {
            _viewStateLiveData.postValue(WebViewFragmentState.Finish)
        }
    }

    fun webViewGoForward(webView: WebView) {
        if (webView.canGoForward()) {
            webView.goForward()
        }
    }

    fun webViewReload(webView: WebView) {
            webView.reload()

    }

    fun checkPermission(activity: Activity) {
        checkPermissions.checkNotificationPermission(activity)
    }

    private fun showLoading() {
        _viewStateLiveData.postValue(WebViewFragmentState.Loading)
    }

    private fun showView(url: String, startUrl:String) {
        _viewStateLiveData.postValue(
            WebViewFragmentState.ShowView(
                url,
                startUrl
            )
        )
    }

    fun goToHomePage(webView: WebView) {
        interactor.loadUrl(webView)
        urlList.clear()
    }

    fun networkStatus(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkStatusCallback)
    }

    fun loadUrl(webView: WebView) {
        interactor.loadUrl(webView)
    }

    fun handleIntent(intent: Intent) {
        interactor.handleIntent(intent)
    }

    fun sendReport(context: Context, currentUrl: String) {
        YandexMetrica.getReporter(context, App.APP_METRICA_KEY).reportEvent(currentUrl)
    }

    fun getUniqueLink() {
        if (uniqueLinkStorage.doRequest().isNotEmpty()) {
            uniqueLink = uniqueLinkStorage.doRequest().get(0)
            Log.d("uniqueLink", "myFirebaseMessagingService2:" + uniqueLink)
            uniqueLinkStorage.clear()
        }
        Log.d("uniqueLink", "myFirebaseMessagingService:" + uniqueLink)

        Log.d("uniqueLink", "uniqueLink :" + uniqueLink.toString())

    }
    fun loadUniqueLink() {
        viewModelScope.launch {
            delay(500)
            when (interactor.checkVpn()) {
                true -> {
                    uniqueLink = null}
                else -> {
                    if (uniqueLink.isNullOrEmpty()) {

                    } else {
                        uniqueLink?.let { webView.loadUrl(it) }
                        Log.d("uniqueLink2", uniqueLink.toString())
                        uniqueLink = null
                    }


                }

            }
        }
    }
    fun getStarUrl(url:String):String{
         when(interactor.checkVpn()){
            true ->{if(startUrlVpn==null){
                startUrlVpn = url
            }
                return startUrlVpn!!}
            else-> {if(startUrl==null){
                startUrl= url

            }
                return startUrl!!}
        }

    }
    private fun checkVpn() {
        viewModelScope.launch {
            val newVpnStatus = interactor.checkVpn()
            if (currentVpnStatus != newVpnStatus) {
                _viewStateLiveData.postValue(WebViewFragmentState.Loading)
                interactor.loadUrl(webView)
                webView.clearHistory()}
            currentVpnStatus = newVpnStatus
        }
    }

}
