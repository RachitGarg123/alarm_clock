package com.example.alarmclock.alarmset.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alarm")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val alarmName: String = "",
    val alarmTime: String,
    var isActive: Boolean = true,
    val isScheduled: Boolean = false,
    val scheduleDate: String = "",
    val isRepeating: Boolean = false,
//    val repeatingDays: List<String> = listOf(),
    val isPm: Boolean
)