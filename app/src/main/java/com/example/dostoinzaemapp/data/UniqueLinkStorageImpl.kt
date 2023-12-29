package com.example.dostoinzaemapp.data

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.dostoinzaemapp.data.localStorage.UniqueLinkStorage
import com.google.gson.Gson

class UniqueLinkStorageImpl(private val sharedPref: SharedPreferences):UniqueLinkStorage {
    companion object {
        const val UNIQUE_LINK_KEY = "UNIQUE_LINK_KEY"
        const val FALSE = "false"
    }
    override fun doRequest(): Array<String> {
        val json = sharedPref.getString(UNIQUE_LINK_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<String>::class.java)

    }

    override fun doWrite(deepLink: List<String>) {
        val json = Gson().toJson(deepLink)
        sharedPref.edit().putString(UNIQUE_LINK_KEY, json).apply()
    }


    @SuppressLint("CommitPrefEdits")
    override fun clear() {
        sharedPref.edit().clear().apply()
    }
}