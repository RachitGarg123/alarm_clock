package com.example.alarmclock.alarmset.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Query("Select * from alarm")
    fun getAlarm(): Flow<MutableList<Alarm>>

    @Insert
    suspend fun addAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Update
    suspend fun updateAlarm(alarm: Alarm)
}