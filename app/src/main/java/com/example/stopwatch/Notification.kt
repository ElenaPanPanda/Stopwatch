package com.example.stopwatch

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat


object Notification {

    private fun createNotificationChannel(context: Context): String {
        var channelID = ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification"
            val descriptionText = "Time exceeded"
            val importance = NotificationManager.IMPORTANCE_HIGH
            channelID = "org.hyperskill"
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        return channelID
    }


    fun show(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder =
            NotificationCompat.Builder(context, createNotificationChannel(context))
                .setSmallIcon(R.drawable.ic_time_exceeded)
                .setContentTitle("Notification")
                .setContentText("Time exceeded")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pIntent)

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(393939, notificationBuilder.build())

        /*val am: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        am.set(AlarmManager.RTC_WAKEUP, chosenTime, pIntent)*/
    }


}
