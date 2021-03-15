package com.rahulabrol.smsreader.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.rahulabrol.smsreader.MainActivity
import com.rahulabrol.smsreader.R
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by Rahul Abrol on 14/3/21.
 */
@AndroidEntryPoint
class SmsListener : BroadcastReceiver() {

    private val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    private val TAG = "SMSBroadcastReceiver"

    override fun onReceive(context: Context?, intent: Intent) {
        Log.i(TAG, "Intent received: " + intent.action)
        if (SMS_RECEIVED == intent.action) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<Any>?
                val messages = arrayOfNulls<SmsMessage>(
                    pdus!!.size
                )
                for (i in pdus.indices) {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                }
                if (messages.size > -1) {
                    Log.i(TAG, "Message recieved: " + messages[0]!!.messageBody)
                    sendNotification(
                        context!!,
                        messages[0]!!.originatingAddress!!,
                        messages[0]!!.messageBody
                    )
                }
            }
        }
    }

    private fun sendNotification(context: Context, title: String, body: String) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val packageName = context.packageName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                packageName,
                packageName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.WHITE
            }
            channel.description = packageName
            notificationManager.createNotificationChannel(channel)
        }
        val notificationBuilder = NotificationCompat.Builder(context, packageName)
        notificationBuilder.setSmallIcon(getNotificationIcon())
            .setColor(ContextCompat.getColor(context, android.R.color.holo_blue_bright))
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .priority = NotificationCompat.PRIORITY_HIGH

        val backIntent = Intent(context, MainActivity::class.java)
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivities(
            context,
            0,
            arrayOf(backIntent),
            PendingIntent.FLAG_ONE_SHOT
        )
        notificationBuilder.setContentIntent(pendingIntent)
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun getNotificationIcon(): Int {
        return R.drawable.ic_message
    }

}