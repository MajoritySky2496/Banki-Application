package com.example.bankiapplication.util.notification
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class PushService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Token", "Refreshed token: $token")

        sendRegistrationToServer(token)

    }
    private fun sendRegistrationToServer(token: String?) {
        Log.d("Token", "sendRegistrationTokenToServer($token)")
    }

}