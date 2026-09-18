package com.example.alarmclock.home.domain.utility.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.alarmclock.R

class AlarmReceiver: BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = 1001
        const val ACTION_DISMISS = "NOTIFICATION_DISMISSED"
        const val NOTIFICATION_CHANNEL_ID = "ALARM_CHANNEL_ID"

        var ringtone: Ringtone? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(intent?.action == ACTION_DISMISS) {
            ringtone?.stop()
            notificationManager.cancel(NOTIFICATION_ID)
            return
        }
        val ringToneUri = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_ALARM
        )
        ringtone = RingtoneManager.getRingtone(
            context,
            ringToneUri
        )
        context?.let {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Alarms",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
            val dismissIntent = Intent(context, AlarmReceiver::class.java).apply {
                action = ACTION_DISMISS
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                10000,
                dismissIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val notification = NotificationCompat
                .Builder(it, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Alarm")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("Your Alarm is Ringing")
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(0, "dismiss", pendingIntent)
                .setOngoing(true)
                .build()
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
        ringtone?.play()

    }
}