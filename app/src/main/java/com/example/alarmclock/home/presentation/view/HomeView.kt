package com.example.alarmclock.home.presentation.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alarmclock.alarmset.presentation.view.BottomSheet
import com.example.alarmclock.home.presentation.model.Alarm
import com.example.alarmclock.home.domain.utility.getTwelveHourFormat

@Composable
fun HomeView(
    padding: PaddingValues,
    showClockUI: () -> Unit,
    alarms: MutableList<Alarm>
) {
    var showDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Alarms",
                fontSize = 22.sp
            )
            Box {
                IconButton(onClick = {
                    expanded = true
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "ScreenSaver")
                        },
                        onClick = {
                            Toast.makeText(context, "ScreenSaver Clicked" , Toast.LENGTH_LONG).show()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Settings")
                        },
                        onClick = {
                            Toast.makeText(context, "Settings Clicked" , Toast.LENGTH_LONG).show()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Privacy Policy")
                        },
                        onClick = {
                            Toast.makeText(context, "Privacy Policy Clicked" , Toast.LENGTH_LONG).show()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Send Feedback")
                        },
                        onClick = {
                            Toast.makeText(context, "Send Feedback Clicked" , Toast.LENGTH_LONG).show()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Help")
                        },
                        onClick = {
                            Toast.makeText(context, "Help Clicked" , Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(
                items = alarms,
            ) { index, item ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = if(alarms.size -1 == index) 120.dp else 0.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp, pressedElevation = 10.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(22.dp)
                    ) {
                        Text(
                            text = if(item.isScheduled) item.scheduleDate else "Not Scheduled",
                            fontSize = 14.sp
                        )
                        Text(
                            text = item.alarmName,
                            fontSize = 14.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = getTwelveHourFormat(item.alarmTime),
                                fontSize = 40.sp
                            )
                            Text(
                                text = if(item.isPm) "pm" else "am",
                                fontSize = 10.sp,
                                modifier = Modifier.offset(y = 6.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Switch(checked = item.isActive, onCheckedChange = { switchStateChange ->
                                Log.d("switchState", "switchStateChange ---> $switchStateChange")
                                alarms[index] = item.copy(isActive = switchStateChange)
                            })
                        }
                    }

                }
            }
        }
    }
    Box(modifier = Modifier.padding(padding).fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier.padding(30.dp)
                .size(80.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                showClockUI()
//                showDialog = true
            },
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Alarm"
            )
        }
        if(showDialog) {
            BottomSheet {
                showDialog = false
            }
        }
    }

}
