package com.example.bankiapplication.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log


class VpnStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            context?.let {
                if (isVpnConnected(it)) {
                    Log.d("VPN Status", "VPN Connected")
                    // Действия при подключении к VPN
                } else {
                    Log.d("VPN Status", "VPN Disconnected")
                    // Действия при отключении от VPN
                }
            }
        }
    }

    fun isVpnConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

            return networkCapabilities != null &&
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        } else {
            // Для более старых версий Android
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val method = systemPropertiesClass.getMethod("get", String::class.java)
            val vpnPropName = "vpn.state"

            val result = method.invoke(null, vpnPropName) as String
            return result == "true"
        }
    }
}