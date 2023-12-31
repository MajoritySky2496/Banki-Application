package com.example.bankiapplication.ui.model

sealed interface WebViewFragmentState{
    object Finish:WebViewFragmentState
    object Loading:WebViewFragmentState
    data class ShowView( val url:String, val startUrl:String):WebViewFragmentState
    object NoConnection:WebViewFragmentState
    object InternetAvailable:WebViewFragmentState

}