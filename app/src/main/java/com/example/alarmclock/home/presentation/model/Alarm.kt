package com.example.alarmclock.home.presentation.model

data class Alarm(
    val alarmName: String = "",
    val alarmTime: String = "",
    var isActive: Boolean = true,
    val isScheduled: Boolean,
    val scheduleDate: String,
    val isRepeating: Boolean,
    val repeatingDays: List<String>
)