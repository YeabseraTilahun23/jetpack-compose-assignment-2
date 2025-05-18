package com.example.doit.ui.screens

import android.app.DatePickerDialog
import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doit.viewmodel.TodoViewModel
import com.example.doit.model.TodoEntity
import java.util.*

@Composable
fun CreateTaskScreen(vm: TodoViewModel, navController: NavController) {
    var title by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("Medium") }
    var priority by remember { mutableStateOf("3") }
    var subtasksText by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDatePicker) {
        val calendar = Calendar.getInstance().apply { timeInMillis = dueDate }
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val cal = Calendar.getInstance().apply { set(year, month, day, 0, 0, 0) }
                dueDate = maxOf(cal.timeInMillis, System.currentTimeMillis())
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
        }.show()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Task Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Due Date: ${DateFormat.format("yyyy-MM-dd", dueDate)}",
                fontSize = 16.sp
            )
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.DateRange, contentDescription = "Date Picker")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        var expandedDifficulty by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = difficulty,
                onValueChange = {},
                label = { Text("Difficulty") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedDifficulty = true },
                enabled = false
            )
            DropdownMenu(
                expanded = expandedDifficulty,
                onDismissRequest = { expandedDifficulty = false }
            ) {
                listOf("Easy", "Medium", "Hard").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            difficulty = option
                            expandedDifficulty = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        var expandedPriority by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = priority,
                onValueChange = {},
                label = { Text("Priority (1-5)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedPriority = true },
                enabled = false
            )
            DropdownMenu(
                expanded = expandedPriority,
                onDismissRequest = { expandedPriority = false }
            ) {
                (1..5).forEach { num ->
                    DropdownMenuItem(
                        text = { Text(num.toString()) },
                        onClick = {
                            priority = num.toString()
                            expandedPriority = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = subtasksText,
            onValueChange = { subtasksText = it },
            label = { Text("Subtasks (comma separated)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (title.isNotEmpty()) {
                        val prio = priority.toIntOrNull() ?: 3
                        val subs = if (subtasksText.isNotEmpty())
                            subtasksText.split(",").map { it.trim() }
                        else emptyList()
                        val newId = (vm.todos.value.maxOfOrNull { it.id } ?: 0) + 1
                        val newTodo = TodoEntity(
                            id = newId,
                            userId = 0,
                            title = title,
                            completed = false,
                            dueDate = dueDate,
                            difficulty = difficulty,
                            priority = prio,
                            subtasks = subs
                        )
                        vm.addTodo(newTodo)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Create")
            }
        }
    }
}
