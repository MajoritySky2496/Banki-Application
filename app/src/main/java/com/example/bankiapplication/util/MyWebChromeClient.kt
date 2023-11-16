package com.example.bankiapplication.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

class MyWebChromeClient(private val activity: Activity) : WebChromeClient() {
    var FilePathCallback: ValueCallback<Array<Uri>>? = null
    private var acceptType: String? = null


    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        this.FilePathCallback = filePathCallback
        this.acceptType = fileChooserParams?.acceptTypes?.getOrNull(0)
        val checkPermissions = CheckPermissions(webView!!.context)
        if (checkPermissions.checkCameraPermission(
                activity
            )
        ) {
            startFileChooser(activity)
        } else {
            checkPermissions.requestPermissions(activity)
            filePathCallback?.onReceiveValue(null)
        }

        return true
    }

    private fun startFileChooser(activity: Activity) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val intentArray: Array<Intent?> = takePictureIntent?.let {
            arrayOf(takePictureIntent)
        } ?: arrayOfNulls(0)
        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
        contentSelectionIntent.type = "*/*"
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Выберите файл")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
        activity.startActivityForResult(chooserIntent, FILE_CHOOSER_REQUEST_CODE)
    }

    companion object {
        const val FILE_CHOOSER_REQUEST_CODE = 2
    }

}



