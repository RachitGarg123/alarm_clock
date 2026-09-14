package com.example.alarmclock.alarmset.presentation.view

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.isPm
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockUI(
    clockDismissed: () -> Unit,
    onTimeSelected: (Int, Int, Boolean) -> Unit
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false
    )
    var showAnalogClock by remember { mutableStateOf(true) }
    Dialog(
        onDismissRequest = { clockDismissed() },
        properties = DialogProperties()
    ) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(),
            shape = RoundedCornerShape(size = 20.dp)
        ) {
            Column(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background).padding(12.dp),
            ) {
                Text(
                    text = "Select time"
                )
                Spacer(modifier = Modifier.size(12.dp))
                if(showAnalogClock) {
                    TimePicker(state = timePickerState)
                } else {
                    TimeInput(state = timePickerState)
                }
                Spacer(modifier = Modifier.size(20.dp))
                Row {
                    Icon(
                        imageVector = if(showAnalogClock) Icons.Default.Keyboard else Icons.Default.LockClock,
                        contentDescription = null,
                        Modifier.clickable(
                            enabled = true,
                            onClick = {
                                showAnalogClock = !showAnalogClock
                            }
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Cancel",
                        Modifier.clickable(
                            enabled = true,
                            onClick = {
                                clockDismissed()
                            }
                        )
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(
                        text = "OK",
                        Modifier.clickable(
                            enabled = true,
                            onClick = {
                                onTimeSelected(
                                    timePickerState.hour,
                                    timePickerState.minute,
                                    timePickerState.isPm
                                )
                            }
                        )
                    )
                }
            }
        }
    }
}