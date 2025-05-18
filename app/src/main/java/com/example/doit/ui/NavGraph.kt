package com.example.doit.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument // ✅ Fix unresolved reference issue
import com.example.doit.ui.screens.CreateTaskScreen
import com.example.doit.ui.screens.DetailScreen
import com.example.doit.ui.screens.HomeScreen
import com.example.doit.viewmodel.TodoViewModel

@Composable
fun NavGraph(vm: TodoViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(vm, navController) }
        composable(
            "detail/{todoId}",
            arguments = listOf(navArgument("todoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("todoId") ?: 0
            DetailScreen(id, vm, navController)
        }
        composable("create") { CreateTaskScreen(vm, navController) }
    }
}
