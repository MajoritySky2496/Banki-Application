package com.example.bankiapplication.util

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import java.io.File
import java.io.FileOutputStream

class Convector(private val activity: Activity) {
     fun uriConvector(bundle:Bundle?):Array<Uri>{
        val bitmap = bundle?.get("data") as Bitmap
        val file = File(activity.cacheDir, "image.jpg")
        val outputStream = FileOutputStream(file)
        val uri = Uri.fromFile(file)
        val array = arrayOf(uri)
        file.createNewFile()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return array
    }
}