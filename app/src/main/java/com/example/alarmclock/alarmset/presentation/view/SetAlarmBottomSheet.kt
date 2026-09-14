package com.example.alarmclock.alarmset.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(dismissed: () -> Unit) {
    var isVibrate by remember { mutableStateOf(true) }
    ModalBottomSheet(
        onDismissRequest = {
            dismissed()
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 34.dp, vertical = 2.dp)) {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "11:59",
                    fontFamily = FontFamily.Default,
                    fontStyle = FontStyle.Normal,
                    fontSize = 40.sp
                )
                Text(
                    text = "pm",
                    fontSize = 10.sp,
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val list = listOf("M", "T", "W", "T", "F", "S", "S")
                list.forEach {
                    ElevatedCard(shape = RoundedCornerShape(10.dp)) {
                        Text(
                            modifier = Modifier.background(color = Color.Black)
                                .padding(horizontal = 15.dp, vertical = 10.dp),
                            text = it
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Alarms Off"
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "Schedule alarm"
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Alarm Name"
                )
                Text(
                    text = "Alarm"
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Sound"
                )
                Text(
                    text = "Song Name"
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Vibrate"
                )
                Switch(
                    checked = isVibrate,
                    onCheckedChange = { switchChanged ->
                        isVibrate = switchChanged
                    }
                )
            }
            Spacer(modifier = Modifier.padding(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {}
                ) {
                    Text(text = "Delete")
                }
                Button(
                    onClick = {}
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}
