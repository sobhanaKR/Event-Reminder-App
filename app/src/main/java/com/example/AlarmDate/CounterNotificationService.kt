package com.example.AlarmDate

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat

class CounterNotificationService (
    private val context: Context
        ){
private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(name: String,counter: Int ) {
        val activityIntent = Intent(context, AlarmDateActivity::class.java)
        val pendingActivity = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val incrementIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context,
            CounterNotificationService::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
         val notification = NotificationCompat.Builder(
             context,
             COUNTER_CHANNEL_ID
         ).setSmallIcon(R.drawable.ic_baseline_calendar_month_24)
             .setContentTitle("Happy Birthday $name")
             .setContentText("Birthdays may come and go $counter")
             .setContentIntent(pendingActivity)
             .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +context.packageName+"/"+R.raw.happy_birthday_notif))
             .addAction(R.drawable.ic_baseline_calendar_month_24,
             "Increment",
                 incrementIntent
             )
             .build()

        notificationManager.notify(1,
        notification)
    }
     companion object {
         const val COUNTER_CHANNEL_ID = "counter_channel"
     }
}