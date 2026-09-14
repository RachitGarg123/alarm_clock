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
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.alarmclock.alarmset.presentation.view.ClockUI
import androidx.compose.ui.Modifier
import com.example.alarmclock.home.domain.utility.receiver.AlarmReceiver
import com.example.alarmclock.home.presentation.model.Alarm
import com.example.alarmclock.ui.theme.AlarmClockTheme
import com.example.alarmclock.home.presentation.view.HomeView
import java.time.LocalDateTime
import java.time.ZoneId

class MainActivity : ComponentActivity() {
    val ALARM_BROADCAST_RECEIVER_REQUEST_CODE = 949
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val alarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager
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
                var showClockUI by remember { mutableStateOf(false) }
                val alarms = remember { mutableStateListOf<Alarm>() }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeView(
                        innerPadding,
                        showClockUI = {
                            showClockUI = true
                        },
                        alarms
                    )
                    if(showClockUI) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ClockUI(
                                onTimeSelected = { hour, minute, isPm ->
                                    val alarmTime = when {
                                        (hour.toString().length == 2 && minute.toString().length == 2) -> "${hour}:${minute}"
                                        (hour.toString().length == 1 && minute.toString().length == 2) -> "0$hour:$minute"
                                        (hour.toString().length == 2 && minute.toString().length == 1) -> "$hour:0$minute"
                                        else -> "0$hour:0$minute"
                                    }
                                    alarms.add(
                                        Alarm(
                                            alarmTime = alarmTime,
                                            isPm = isPm
                                        )
                                    )
                                    showClockUI = false
                                },
                                clockDismissed = {
                                    showClockUI = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


