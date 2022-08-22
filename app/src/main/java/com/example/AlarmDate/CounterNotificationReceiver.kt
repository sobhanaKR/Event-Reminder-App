package com.example.AlarmDate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.AlarmDate.data.Counter

class CounterNotificationReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
       val service = p0?.let { CounterNotificationService(it) }
        service?.showNotification("sobhana",++Counter.value)
    }
}