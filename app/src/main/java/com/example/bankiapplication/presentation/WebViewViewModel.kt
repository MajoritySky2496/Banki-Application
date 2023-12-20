package com.example.bankiapplication.presentation
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankiapplication.domain.Interactor
import com.example.bankiapplication.ui.model.WebViewFragmentState
import com.example.bankiapplication.util.CheckPermissions
import com.example.bankiapplication.util.app.App
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.yandex.metrica.YandexMetrica

class WebViewViewModel(
    private val interactor: Interactor,
    private val checkPermissions: CheckPermissions,


) : ViewModel() {
    lateinit var webView: WebView
    private var urlList = mutableListOf<String>()
    private var startUrl:String? = null
    private val networkStatusCallback = object :ConnectivityManager.NetworkCallback(){
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
    private val webViewClient = object :WebViewClient(){
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showLoading()
            showView(url!!)
            Log.d("myLog", "start")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            Log.d("myLog", "finish")

        }
    }

    private var _viewStateLiveData = MutableLiveData<WebViewFragmentState>()
    val viewStateLiveData: LiveData<WebViewFragmentState> = _viewStateLiveData

    init {
        getToken()
    }

     fun showWebView(webView: WebView) {
         this.webView = webView
         when(interactor.checkConnected()){
             500 -> {_viewStateLiveData.postValue(WebViewFragmentState.NoConnection)
                 interactor.startWebView(webView, webViewClient)}
             200 -> interactor.startWebView(webView, webViewClient)
         }
    }

    fun webViewGoBack(webView: WebView) {
        webView.goBack()
    }

    fun finish(webView: WebView) {
        if (webView.canGoBack()) {
            webView.goBack()
        }else{
            _viewStateLiveData.postValue(WebViewFragmentState.Finish)
        }
    }

    fun webViewGoForward(webView: WebView) {
        if (webView.canGoForward()) {
            webView.goForward()
        }
    }

    fun webViewReload(webView: WebView) {
        if(urlList.size>=1){
            webView.reload()
        }else{
            interactor.loadUrl(webView)
        }
    }

    fun checkPermission(activity: Activity) {
        checkPermissions.checkNotificationPermission(activity)
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
    private fun showLoading(){
        _viewStateLiveData.postValue(WebViewFragmentState.Loading)
    }
    private fun showView(url: String){
        when(interactor.checkVpn()){
            true -> {if(urlList.size>=1) loadUrl(webView)
                urlList.clear()
                _viewStateLiveData.postValue(WebViewFragmentState.ShowViewVpn(url))
            }
            false -> {url.let { urlList.add(it) }
                Log.d("myLog", urlList.toString())
                if(startUrl==null){
                    startUrl = url
                }
                _viewStateLiveData.postValue(WebViewFragmentState.ShowView(url, urlList, startUrl!!))}
        }
    }

    fun goToHomePage(webView: WebView){
        interactor.loadUrl(webView)
        urlList.clear()
    }
    fun networkStatus(context: Context){
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkStatusCallback)
    }
    fun loadUrl(webView: WebView){
        interactor.loadUrl(webView)
    }
    fun handleIntent(intent: Intent){
        interactor.handleIntent(intent)
    }
    fun sendReport(context: Context, currentUrl:String){
        YandexMetrica.getReporter(context, App.APP_METRICA_KEY).reportEvent(currentUrl)
    }

}