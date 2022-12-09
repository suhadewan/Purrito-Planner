package com.example.purritoplanner

import android.R
import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class PushIntentService(name: String?) : IntentService(name) {
    val notificationID: Int = 1
    override fun onHandleIntent(intent: Intent?) {
        Log.d("test", "Got to Push Intent Service")
        val builder: Notification.Builder = Notification.Builder(this)
        builder.setContentTitle("Purrito Planner Notification")
        builder.setContentText("Don\'t Forget to Eat Today! :3")
        builder.setSmallIcon(R.drawable.btn_star)
        builder.setPriority(Notification.PRIORITY_DEFAULT)
        val notifyIntent = Intent(this, SettingFragment::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pendingIntent)
        val notificationCompat: Notification = builder.build()
        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(notificationID, notificationCompat)
    }
}