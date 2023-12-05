package com.example.bankiapplication.ui.model

sealed interface WebViewFragmentState{
    object Finish:WebViewFragmentState
    object ShowToolbar:WebViewFragmentState
    object HideToolbar:WebViewFragmentState
}