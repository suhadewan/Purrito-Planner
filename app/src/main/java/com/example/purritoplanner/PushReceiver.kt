package com.example.purritoplanner

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PushReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("test", "Got to push receiver")

        //Set up the channel we'll host the notification on.
        val channel_id = "PurritoPlannerReminders"
        val name = "Purrito Planner Meal Reminder!"
        val descriptionText = "Don't forget to eat today! :3"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channel_id, name, importance).apply {
            description = descriptionText
        }

        //Register the channel with the system
        val notificationManager: NotificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // Set up the actual notification itself.
        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(context, SettingFragment::class.java), PendingIntent.FLAG_IMMUTABLE)
        val notification = Notification.Builder(context)
            .setContentTitle(name)
            .setContentText(descriptionText)
            .setSmallIcon(R.drawable.btn_star)
            .setChannelId(channel_id)
            .setContentIntent(pendingIntent)

        // Now handle the notification manager.
        notificationManager.notify(123, notification.build())

    }
}