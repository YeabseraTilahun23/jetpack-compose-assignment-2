package com.example.doit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doit.ui.screens.CreateTaskScreen
import com.example.doit.ui.screens.DetailScreen
import com.example.doit.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val vm: TodoViewModel = viewModel()

    NavHost(navController = navController, startDestination = "createTask") {
        composable("createTask") { CreateTaskScreen(vm = vm, navController = navController) }
        composable("detail/{todoId}") { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("todoId")?.toIntOrNull()
            DetailScreen(todoId = todoId, vm = vm, navController = navController)
        }
    }
}
