package com.example.alarmclock.home.domain.utility

import com.example.alarmclock.home.presentation.model.Alarm

fun getTwelveHourFormat(alarmTime: String): String {
    val alarmHour = alarmTime.take(2).toIntOrNull() ?: return "--:--"
    if(alarmHour == 0) {
        return "12 + ${alarmTime.substring(startIndex = 2, endIndex = alarmTime.length)}"
    }
    return if(alarmHour < 13) { // 08:00
        alarmTime.substring(startIndex = 1, endIndex = alarmTime.length)
    } else { //12:35
        "${alarmHour-12}${alarmTime.substring(startIndex = 2, endIndex = alarmTime.length)}"
    }
}

