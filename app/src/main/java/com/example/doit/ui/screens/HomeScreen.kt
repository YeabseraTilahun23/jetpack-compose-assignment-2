package com.example.doit.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doit.viewmodel.TodoViewModel
import com.example.doit.model.TodoEntity

@Composable
fun HomeScreen(vm: TodoViewModel, navController: NavController) {
    val todosList by vm.todos.collectAsState(initial = emptyList())
    val loading by vm.isLoading.collectAsState(initial = false)
    val search by vm.searchQuery.collectAsState(initial = "")
    val sortOpt by vm.sortOption.collectAsState(initial = "")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = search,
                onValueChange = { vm.setSearch(it) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search") }
            )
            IconButton(onClick = { vm.toggleDarkMode() }) {
                Icon(imageVector = Icons.Default.Brightness6, contentDescription = "Toggle mode")
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Sort by: ")
            var expanded by remember { mutableStateOf(false) }
            Box {
                Text(
                    text = sortOpt,
                    modifier = Modifier.clickable { expanded = true }
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    vm.sortOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { vm.setSort(option); expanded = false }
                        )
                    }
                }
            }
        }

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(todosList) { todo ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable { navController.navigate("detail/${todo.id}") },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(todo.title, fontSize = 18.sp)
                                Text("Priority: ${todo.priority}  Difficulty: ${todo.difficulty}", fontSize = 14.sp)
                                Text("Due: " + android.text.format.DateFormat.format("yyyy-MM-dd", todo.dueDate), fontSize = 14.sp)
                            }
                            IconButton(onClick = { vm.removeTodo(todo) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("create") },
            modifier = Modifier.padding(16.dp).align(Alignment.End)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
        }
    }
}
