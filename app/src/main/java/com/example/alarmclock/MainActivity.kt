package com.example.alarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.alarmclock.home.domain.utility.receiver.AlarmReceiver
import com.example.alarmclock.ui.theme.AlarmClockTheme
import com.example.alarmclock.home.presentation.view.HomeView
import java.time.LocalDateTime
import java.time.ZoneId

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val alarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager
        val ALARM_BROADCAST_RECEIVER_REQUEST_CODE = 949
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            val canScheduleAlarm = alarmManager.canScheduleExactAlarms()
            if(canScheduleAlarm) {
                val alarmIntent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    ALARM_BROADCAST_RECEIVER_REQUEST_CODE,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                val alarmTime = LocalDateTime.now().plusSeconds(20)
                val triggerTime = alarmTime
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
                val alarmClockInfo = AlarmManager.AlarmClockInfo(
                    triggerTime,
                    pendingIntent
                )
                alarmManager.setAlarmClock(
                    alarmClockInfo,
                    pendingIntent
                )
            }
        }
        setContent {
            AlarmClockTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeView(innerPadding)
                }
            }
        }
    }
}


