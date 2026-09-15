package com.example.alarmclock.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarmclock.alarmset.data.db.Alarm
import com.example.alarmclock.alarmset.data.db.AlarmDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: AlarmDao
): ViewModel() {
    val alarms: StateFlow<MutableList<Alarm>> =
        dao.getAlarm()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = mutableListOf()
            )

    fun addAlarm(alarm: Alarm) = viewModelScope.launch {
        dao.addAlarm(alarm)
    }
}