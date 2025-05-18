package com.example.doit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doit.viewmodel.TodoViewModel
import com.example.doit.model.TodoEntity

@Composable
fun TodoDetailsScreen(todo: TodoEntity) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Title: ${todo.title}", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("User ID: ${todo.userId}", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Status: " + if (todo.completed) "Completed" else "Pending", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Due Date: " + android.text.format.DateFormat.format("yyyy-MM-dd", todo.dueDate), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Difficulty: ${todo.difficulty}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Priority: ${todo.priority}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Subtasks: " + if (todo.subtasks.isEmpty()) "None" else todo.subtasks.joinToString(", "))
    }
}

@Composable
fun DetailScreen(todoId: Int?, vm: TodoViewModel, navController: NavController) {
    val todosList = vm.todos.collectAsState(initial = emptyList()).value

    val todo = remember(todoId) {
        derivedStateOf { todosList.find { it.id == todoId } }
    }

    if (todo.value == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Task not found")
        }
    } else {
        val task = todo.value!!

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Title: ${task.title}", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("User ID: ${task.userId}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Status: " + if (task.completed) "Completed" else "Pending", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Due Date: " + android.text.format.DateFormat.format("yyyy-MM-dd", task.dueDate), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Difficulty: ${task.difficulty}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Priority: ${task.priority}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Subtasks: " + if (task.subtasks.isEmpty()) "None" else task.subtasks.joinToString(", "))
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = { navController.popBackStack() }, modifier = Modifier.weight(1f)) { Text("Back") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { vm.removeTodo(task); navController.popBackStack() }, modifier = Modifier.weight(1f)) { Text("Delete") }
            }
        }
    }
}
