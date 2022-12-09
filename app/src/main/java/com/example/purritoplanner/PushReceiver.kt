package com.example.purritoplanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PushReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent(context, PushIntentService::class.java)
        context!!.startService(intent)
        Log.d("test", "Got to push receiver")
    }
}