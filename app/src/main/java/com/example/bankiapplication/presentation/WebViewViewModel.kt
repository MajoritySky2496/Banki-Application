package com.example.bankiapplication.presentation
import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankiapplication.domain.Interactor
import com.example.bankiapplication.ui.model.WebViewFragmentState
import com.example.bankiapplication.util.CheckPermissions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class WebViewViewModel(
    private val interactor: Interactor,
    private val checkPermissions: CheckPermissions

) : ViewModel() {

    private var urlList = mutableListOf<String>()
    private val webViewClient = object :WebViewClient(){
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showLoading()
            url?.let { urlList.add(it) }
            Log.d("myLog", "start")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            showView(url!!)
            Log.d("myLog", "finish")

        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)

        }
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return super.shouldOverrideUrlLoading(view, url)
        }
    }

    private var _viewStateLiveData = MutableLiveData<WebViewFragmentState>()
    val viewStateLiveData: LiveData<WebViewFragmentState> = _viewStateLiveData

    init {
        getToken()
    }

     fun showWebView(webView: WebView) {
         when(interactor.checkConnected()){
             500 -> _viewStateLiveData.postValue(WebViewFragmentState.NoConnection)
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
        webView.reload()
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
        _viewStateLiveData.postValue(WebViewFragmentState.ShowView(url, urlList, getStartUrl()))
    }
    private fun getStartUrl():String{
        return interactor.getStartUrl()

    }
    fun goToHomePage(webView: WebView){
        while (webView.canGoBack()){
            webView.goBack()
        }
    }
}