package com.example.alarmclock.home.domain.utility.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val ringToneUri = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_ALARM
        )
        val ringtone = RingtoneManager.getRingtone(
            context,
            ringToneUri
        )
        ringtone.play()
    }
}