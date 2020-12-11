package com.loyalie.connectre.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.loyalie.connectre.R
import com.loyalie.connectre.data.PreferenceStorage
import com.loyalie.connectre.ui.splash.SplashActivity
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PushNotification @Inject constructor(
    private val notificationManager: NotificationManager,
    private val preferenceStore: PreferenceStorage
) {


    fun show(context: Context, data: Map<String, String>) {
        if (data.isNotEmpty()) {
            val message = JSONObject(data.iterator().next().value)
            if (message.has("msg")) {
                val msgData = message.getJSONObject("msg")
                if (msgData.has("prog_type")) {
                    val type = msgData.get("prog_type") as Int
                    val programId = msgData.getString("prog_id") ?: ""
                    val title = msgData.getString("title")
                    val desc = msgData.getString("data")
                    /*   val dataIntent = when (type) {
                           OFFER_TYPE -> {
                               Intent(context, OfferDetailsActivity::class.java).apply {
                                   putExtra(OfferDetailsActivity.PROGRAM_ID, programId)
                                   putExtra(OfferDetailsActivity.REFERAL_LINK, "")
                                   putExtra(OfferDetailsActivity.PROGRAM_NAME, "")
                                   putExtra(OfferDetailsActivity.PROGRAM_DESC, "")
                                   putExtra(FROM_PUSH, true)
                                   flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                                           or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                           or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                           or Intent.FLAG_ACTIVITY_NEW_TASK)
                               }


                           }

                           LEAD_ADDED_TYPE -> {
                               Intent(context, LeadStatusDetailsActivity::class.java).apply {
                                   putExtra(LeadStatusDetailsActivity.PROGRAM_ID, programId)
                                   putExtra(LeadStatusDetailsActivity.DESCRIPTION, "")
                                   putExtra(LeadStatusDetailsActivity.NAME, "")
                                   putExtra(LeadStatusDetailsActivity.IMAGE, "")
                                   putExtra(FROM_PUSH, true)
                                   flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                                           or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                           or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                           or Intent.FLAG_ACTIVITY_NEW_TASK)
                               }

                           }

                           else -> {*/
                    val dataIntent = Intent(context, SplashActivity::class.java).apply {
                        flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                                or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                or Intent.FLAG_ACTIVITY_NEW_TASK)
//                            }
//                        }
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (!notificationChannelExists(MESSGE)) {
                            createChannel(context, MESSGE, "Messages")
                        }
                    }


                    val pendingIntent = PendingIntent.getActivity(
                        context, 0 /* Request code */, dataIntent,
                        PendingIntent.FLAG_ONE_SHOT
                    )
                    if (preferenceStore.loginStatus) sendNotification(
                        context,
                        pendingIntent,
                        title,
                        desc
                    )
                }
            }


        }


    }

    private fun sendNotification(
        context: Context,
        pendingIntent: PendingIntent,
        title: String,
        desc: String
    ) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, MESSGE)
            .setSmallIcon(R.drawable.app_logo_small)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher_logo
                )
            )
            .setColor(ContextCompat.getColor(context, R.color.black))
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(title)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(desc)
            )
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(context: Context, channelId: String, channelTitle: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val notificationChannel = NotificationChannel(channelId, channelTitle, importance)
        notificationChannel.setShowBadge(true)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(notificationChannel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun notificationChannelExists(channelId: String): Boolean =
        notificationManager.getNotificationChannel(channelId) != null

    companion object {
        const val MESSGE = "message"
        const val LOYALIE_TYPE = 1
        const val OFFER_TYPE = 2
        const val LEAD_ADDED_TYPE = 3
        const val LEAD_CLAIMED_TYPE = 0
    }
}