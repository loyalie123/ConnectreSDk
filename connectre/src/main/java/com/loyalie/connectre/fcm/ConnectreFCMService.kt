package com.loyalie.connectre.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.loyalie.connectre.data.UserIndependentPref
import dagger.android.AndroidInjection
import javax.inject.Inject

class ConnectreFCMService : FirebaseMessagingService() {

    @Inject
    lateinit var pushNotification: PushNotification
    @Inject
    lateinit var pref: UserIndependentPref


    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        p0?.data?.let { pushNotification.show(this@ConnectreFCMService, it) }

    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
//        Log.d("token",p0)
        pref.fcmToken = p0 ?: ""
    }


}