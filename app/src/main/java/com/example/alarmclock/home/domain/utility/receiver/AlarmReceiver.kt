package com.example.alarmclock.home.domain.utility.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.alarmclock.R

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val ringToneUri = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_ALARM
        )
        val ringtone = RingtoneManager.getRingtone(
            context,
            ringToneUri
        )
            val NOTIFICATION_CHANNEL_ID = "ALARM_CHANNEL_ID"
            context?.let {
                val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Alarms",
                    NotificationManager.IMPORTANCE_HIGH
                )
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(notificationChannel)
                val notification = NotificationCompat
                    .Builder(it, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("Alarm")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText("Your Alarm is Ringing")
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOngoing(true)
                    .build()
                notificationManager.notify(1001, notification)
            }
            ringtone.play()

    }
}