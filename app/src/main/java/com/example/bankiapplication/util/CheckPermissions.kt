package com.example.bankiapplication.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CheckPermissions(private val context: Context) {
    private val NOTIFICATION_PERMISSION_CODE = 100
    private val PERMISSION_REQUEST_CODE = 2


    fun checkNotificationPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_CODE
            )
        }
    }

    fun checkCameraPermission(activity: Activity): Boolean {
        val cameraPermissions =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        return cameraPermissions == PackageManager.PERMISSION_GRANTED
    }
    fun requestPermissions(activity: Activity){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

}