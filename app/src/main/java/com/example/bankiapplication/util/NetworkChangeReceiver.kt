package com.example.bankiapplication.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkChangeReceiver(private val listener:NetworkChangeListener):BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        when(checkInternet(context!!)){
            300 -> listener.onVpnOn()
            500 -> listener.onNetworkUnavailable()
            200 -> listener.onNetworkAvailable()
        }
        if(isConnected(context!!)){
            listener.onNetworkAvailable()
        }else{
            listener.onNetworkUnavailable()
        }
    }
    fun isConnected(context:Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                }
            }
        } else {
            val networks = connectivityManager.allNetworks
            for (i in 0..networks.size - 1) {
                val capabilities = connectivityManager.getNetworkCapabilities(networks[i])
                if(capabilities != null){
                    when{
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                    }
                }
            }

        }
        return false
    }

    fun checkVpn(context:Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            val isVpnConnected =
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
            if(isVpnConnected!=null) {
                return isVpnConnected
            }

        } else {
            val networks = connectivityManager.allNetworks
            for (i in 0..networks.size - 1) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(networks[i])
                val isVpnConnected =
                    networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                return isVpnConnected!!
            }
        }
        return false
    }
    fun checkInternet(context: Context):Int{
        when(checkVpn(context)){
            true -> return 300
            false -> {
                when(isConnected(context)){
                    true -> return 200
                    false -> return 500
                }
            }
        }
    }
}