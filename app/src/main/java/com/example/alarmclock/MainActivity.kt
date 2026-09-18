package com.example.alarmclock

import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.alarmclock.alarmset.presentation.view.ClockUI
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.alarmclock.alarmset.data.db.Alarm
import com.example.alarmclock.home.domain.utility.receiver.AlarmReceiver
import com.example.alarmclock.ui.theme.AlarmClockTheme
import com.example.alarmclock.home.presentation.view.HomeView
import com.example.alarmclock.home.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZonedDateTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val ALARM_BROADCAST_RECEIVER_REQUEST_CODE = 949

    val viewModel: HomeViewModel by viewModels()
    fun checkExactAlarmPermission(alarmManager: AlarmManager): Boolean {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var notificationPermission = mutableStateOf(false)
        val canScheduleAlarm = mutableStateOf<Boolean?>(null)
        enableEdgeToEdge()
        val alarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager
        val exactAlarmSettingsLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            canScheduleAlarm.value = checkExactAlarmPermission(alarmManager)
        }
        val notificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            notificationPermission.value = isGranted
        }

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            canScheduleAlarm.value = alarmManager.canScheduleExactAlarms()
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                when {
                    ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        notificationPermission.value = true
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                        notificationPermission.value = false
                    }
                    else -> {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
        } else {
            canScheduleAlarm.value = true
        }
        setContent {
            AlarmClockTheme {
                if(canScheduleAlarm.value == true) {
                    if(notificationPermission.value) {
                        var showClockUI by remember { mutableStateOf(false) }
                        val alarms by viewModel.alarms.collectAsStateWithLifecycle()
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
                                            val newAlarm = Alarm(
                                                alarmTime = alarmTime,
                                                isPm = isPm
                                            )
                                            alarms.add(
                                                newAlarm
                                            )
                                            viewModel.addAlarm(newAlarm)
                                            showClockUI = false
                                            val alarmIntent = Intent(this@MainActivity, AlarmReceiver::class.java)
                                            val pendingIntent = PendingIntent.getBroadcast(
                                                this@MainActivity,
                                                ALARM_BROADCAST_RECEIVER_REQUEST_CODE,
                                                alarmIntent,
                                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                            )
                                            val now = ZonedDateTime.now()
                                            val todayAlarm = now.withHour(hour)
                                                .withMinute(minute)
                                                .withSecond(0)
                                                .withNano(0)
                                            val alarmTimeLocalDateTime = if(todayAlarm.isAfter(now)) {
                                                todayAlarm
                                            } else {
                                                todayAlarm.plusDays(1)
                                            }
                                            val triggerTime = alarmTimeLocalDateTime
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
                                        },
                                        clockDismissed = {
                                            showClockUI = false
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        Box {
                            AlertDialog(
                                title = {
                                    Text(
                                        text = "Please provide Notification Permission"
                                    )
                                },
                                onDismissRequest = {
                                    finish()
                                },
                                confirmButton = {
                                    Text(
                                        modifier = Modifier.clickable(
                                            enabled = true,
                                            onClick = {
                                                finish()
                                            }
                                        ),
                                        text = "Ok"
                                    )
                                },
                                modifier = Modifier,
                                dismissButton = {
                                    Text(
                                        modifier = Modifier.clickable(
                                            enabled = true,
                                            onClick = {
                                                finish()
                                            }
                                        ),
                                        text = "Cancel"
                                    )
                                }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AlertDialog(
                            onDismissRequest = {
                                finish()
                            },
                            confirmButton = {
                                Text(
                                    text = "OK",
                                    modifier = Modifier.clickable(
                                        enabled = true,
                                        onClick = {
                                            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                                data = "package:${this@MainActivity.packageName}".toUri()
                                            }
                                            exactAlarmSettingsLauncher.launch(intent)
                                        }
                                    )
                                )
                            },
                            modifier = Modifier,
                            dismissButton = {
                                Text(
                                    text = "Cancel",
                                    Modifier.clickable(
                                        enabled = true,
                                        onClick = {
                                            finish()
                                        }
                                    )
                                )
                            },
                            title = {
                                Text(
                                    text = "Please provide the necessary permissions for Setting Alarm",
                                    fontSize = TextUnit(16f, TextUnitType.Sp)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


