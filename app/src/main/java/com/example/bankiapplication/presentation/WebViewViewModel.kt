package com.example.bankiapplication.presentation

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
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

    private var urlHashSetList = HashSet<String>()

    private val webViewClient = object :WebViewClient(){
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            url?.let { urlHashSetList.add(it) }
            showVisibilityOfToolbar(urlHashSetList, url!!)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }
    }

    private var _viewStateLiveData = MutableLiveData<WebViewFragmentState>()
    val viewStateLiveData: LiveData<WebViewFragmentState> = _viewStateLiveData

    init {
        getToken()
    }

    fun showWebView(webView: WebView) {
        interactor.startWebView(webView, webViewClient)
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
    private fun showVisibilityOfToolbar(urlHashSetList:HashSet<String>, url:String){
        Log.d("MyLog", urlHashSetList.toString())
        if(url!="https://credp.site/auto-matic-zaem"){
            _viewStateLiveData.postValue(WebViewFragmentState.ShowToolbar)
        }else{
            _viewStateLiveData.postValue(WebViewFragmentState.HideToolbar)
        }
    }
}