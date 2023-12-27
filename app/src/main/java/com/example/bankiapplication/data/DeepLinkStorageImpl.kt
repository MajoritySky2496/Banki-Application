package com.example.bankiapplication.data

import android.content.SharedPreferences
import com.example.bankiapplication.data.localStorage.DeepLinkStorage
import com.google.gson.Gson

class DeepLinkStorageImpl(val sharedPrefrs: SharedPreferences):DeepLinkStorage {

    companion object {
        const val DEEPLINK_KEY = "DEEPLINK_KEY"
        const val FALSE = "false"
    }
    override fun doRequest(): Array<String> {
        val json = sharedPrefrs.getString(DEEPLINK_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<String>::class.java)
    }

    override fun doWrite(deepLink: List<String>) {
        val json = Gson().toJson(deepLink)
        sharedPrefrs.edit().putString(DEEPLINK_KEY, json).apply()
    }
}