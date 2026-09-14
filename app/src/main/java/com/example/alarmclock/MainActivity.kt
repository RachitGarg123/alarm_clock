package com.example.alarmclock

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.alarmclock.ui.theme.AlarmClockTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlin.math.exp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlarmClockTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeView(innerPadding)
                }
            }
        }
    }
}

@Composable
fun HomeView(padding: PaddingValues) {
    var showDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(modifier = Modifier.padding(padding).padding(horizontal = 12.dp, vertical = 12.dp)) {
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
            val items = getAlarms()
            itemsIndexed(
                items = items,
            ) { index, item ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = if(items.size -1 == index) 120.dp else 0.dp),
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
                                text = getAmOrPm(item.alarmTime),
                                fontSize = 10.sp,
                                modifier = Modifier.offset(y = 6.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Switch(checked = item.isActive, onCheckedChange = { switchStateChange ->
                                item.isActive = switchStateChange
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
                showDialog = true
            },
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Alarm"
            )
        }
        if(showDialog) {
            BottomSheet()
        }
    }

}

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

fun getAmOrPm(alarmTime: String): String {
    val alarmHour = alarmTime.take(2).toIntOrNull()
    return if(alarmHour != null && alarmHour < 12) {
        "am"
    } else {
        "pm"
    }
}

fun getAlarms() = listOf(
    Alarm(
        alarmName = "Demo Alarm Name",
        alarmTime = "23:59",
        isActive = true,
        isScheduled = false,
        isRepeating = false,
        scheduleDate = "",
        repeatingDays = listOf()
    ),
    Alarm(
        alarmName = "Demo Alarm Name",
        alarmTime = "23:59",
        isActive = true,
        isScheduled = false,
        isRepeating = false,
        scheduleDate = "",
        repeatingDays = listOf()

    ),
    Alarm(
        alarmName = "Demo Alarm Name",
        alarmTime = "23:59",
        isActive = true,
        isScheduled = false,
        isRepeating = false,
        scheduleDate = "",
        repeatingDays = listOf()
    ),
    Alarm(
        alarmName = "Demo Alarm Name",
        alarmTime = "23:59",
        isActive = true,
        isScheduled = false,
        isRepeating = false,
        scheduleDate = "",
        repeatingDays = listOf()
    ),
    Alarm(
        alarmName = "Demo Alarm Name",
        alarmTime = "23:59",
        isActive = true,
        isScheduled = false,
        isRepeating = false,
        scheduleDate = "",
        repeatingDays = listOf()
    ),
    Alarm(
        alarmName = "Demo Alarm Name",
        alarmTime = "23:59",
        isActive = true,
        isScheduled = false,
        isRepeating = false,
        scheduleDate = "",
        repeatingDays = listOf()
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet() {
    ModalBottomSheet(
        onDismissRequest = {
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Add Alarm",
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                fontSize = 20.sp
            )
            Spacer(
                modifier = Modifier.padding(20.dp)
            )
            Row {
                Text(
                    text = "Alarm Name"
                )
                TextField(
                    placeholder = {
                        Text(
                            text = "Alarm Name"
                        )
                    },
                    value = "",
                    onValueChange = {}
                )
            }
            Spacer(modifier = Modifier.padding(12.dp))
            Text(
                text = "Alarm Time"
            )
            Spacer(modifier = Modifier.padding(12.dp))
            LazyRow {
                items(
                    items = listOf("S", "M", "T", "W", "T", "F", "S")
                ) { item ->
                    ElevatedCard(shape = RoundedCornerShape(10.dp)) {
                        Text(item)
                    }
                }
            }
            Spacer(modifier = Modifier.padding(12.dp))
            Button(
                onClick = {}
            ) {
                Text(text = "Save")
            }
        }
    }
}

data class Alarm(
    val alarmName: String = "",
    val alarmTime: String = "",
    var isActive: Boolean = true,
    val isScheduled: Boolean,
    val scheduleDate: String,
    val isRepeating: Boolean,
    val repeatingDays: List<String>
)
