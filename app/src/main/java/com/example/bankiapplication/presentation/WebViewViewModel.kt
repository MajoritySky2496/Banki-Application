package com.example.bankiapplication.presentation

import android.app.Activity
import android.util.Log
import android.webkit.WebView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankiapplication.domain.Interactor
import com.example.bankiapplication.ui.model.WebViewFragmentState
import com.example.bankiapplication.util.CheckPermissions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class WebViewViewModel(private val interactor: Interactor, private val checkPermissions: CheckPermissions): ViewModel() {

    private var _viewStateLiveData = MutableLiveData<WebViewFragmentState>()
    val viewStateLiveData: LiveData<WebViewFragmentState> = _viewStateLiveData

    init {
        getToken()
    }

    fun showWebView(webView: WebView){
        interactor.startWebView(webView)
    }
    fun webViewGoBack(webView: WebView) {
        if (webView.canGoBack()) {
            webView.goBack()
        }else{
            _viewStateLiveData.postValue(WebViewFragmentState.Finish)
        }
    }
    fun checkPermission(activity: Activity){
        checkPermissions.checkNotificationPermission(activity)
    }
    private fun getToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("MyLog", token)
        })
    }
}