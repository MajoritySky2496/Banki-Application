package com.example.bankiapplication.util.webview

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.PermissionRequest
import android.webkit.PermissionRequest.RESOURCE_VIDEO_CAPTURE
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.example.bankiapplication.util.Convector
import com.tbruyelle.rxpermissions3.RxPermissions
import java.util.*


class MyWebChromeClient(private val activity: Activity, private val webView: WebView?) :
    WebChromeClient() {

    var filePathCallback: ValueCallback<Array<Uri>>? = null
    private var acceptType: String? = null
    private var convector = Convector(activity)
    private val rxPermissions = RxPermissions(webView!!.context as FragmentActivity)

    val chooser =
        (activity as FragmentActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri = data?.data
                if (uri != null) {
                    val array = arrayOf(uri)
                    filePathCallback?.onReceiveValue(array)
                } else {
                    val bundle = data?.extras
                    val arrayUri = convector.uriConvector(bundle)
                    filePathCallback?.onReceiveValue(arrayUri)
                }

            } else {
                filePathCallback?.onReceiveValue(null)
            }

        }


    @SuppressLint("CheckResult")
    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        this.filePathCallback = filePathCallback
        this.acceptType = fileChooserParams?.acceptTypes?.getOrNull(0)

        rxPermissions
            .request(Manifest.permission.CAMERA)
            .subscribe { granted: Boolean ->
                if (granted) {
                    startFileChooser()
                } else {
                    startFileChooser()
                }
            }
        return true
    }

    private fun startFileChooser() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val intentArray: Array<Intent?> = takePictureIntent.let {
            arrayOf(takePictureIntent)
        } ?: arrayOfNulls(0)
        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
        contentSelectionIntent.type = "*/*"
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Выберите файл")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
        chooser.launch(chooserIntent)


    }

    @SuppressLint("CheckResult")
    override fun onPermissionRequest(request: PermissionRequest?) {
        val resourceArray = arrayOf(RESOURCE_VIDEO_CAPTURE)
        rxPermissions
            .request(Manifest.permission.CAMERA)
            .subscribe { granted: Boolean ->
                if (granted) {
                    request?.grant(resourceArray)
                } else {
                    request?.grant(resourceArray)
                }
            }
    }
}



