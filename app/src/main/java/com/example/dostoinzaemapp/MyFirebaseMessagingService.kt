package com.example.dostoinzaemapp

import android.content.Context
import android.util.Log
import com.example.dostoinzaemapp.data.DeepLinkStorageImpl.Companion.FALSE
import com.example.dostoinzaemapp.data.UniqueLinkStorageImpl
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.yandex.metrica.push.firebase.MetricaMessagingService


class MyFirebaseMessagingService: FirebaseMessagingService() {



    private val gson = Gson()
    var uniqueLink = mutableListOf<String>()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (MetricaMessagingService.isNotificationRelatedToSDK(remoteMessage)) {
                MetricaMessagingService().processPush(this, remoteMessage)
            remoteMessage.data.isNotEmpty().let {
                Log.d(TAG, "Message data payload: " + remoteMessage.data)

                val link = remoteMessage.data.get("yamp")

                Log.d(TAG, "Link:" + link?.get(3))
                val pushObject:PushObject = gson.fromJson(link, PushObject::class.java)
                uniqueLink.add(pushObject.c)
                doWrite(uniqueLink)


                Log.d(TAG, "PushObject:" + pushObject.c)


            }
            remoteMessage.notification?.let {
                Log.d(TAG, "Message Notification Body: ${it.body}")
            }


            }



    }
    private fun doWrite(deepLink: List<String>) {
        val sharedPref = getSharedPreferences(FALSE, Context.MODE_PRIVATE)
        val json = Gson().toJson(deepLink)
        sharedPref.edit().putString(UniqueLinkStorageImpl.UNIQUE_LINK_KEY, json).apply()
    }


    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

    }


    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }


}
data class PushObject(
    val c:String
){

}