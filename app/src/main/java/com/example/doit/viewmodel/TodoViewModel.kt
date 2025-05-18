package com.example.doit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.doit.database.AppDatabase
import com.example.doit.network.TodoApi
import com.example.doit.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TodoViewModel(app: Application): AndroidViewModel(app) {
    private val _todos = MutableStateFlow<List<com.example.doit.model.TodoEntity>>(emptyList())
    val todos: StateFlow<List<com.example.doit.model.TodoEntity>> get() = _todos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMsg = MutableStateFlow("")
    val errorMsg: StateFlow<String> get() = _errorMsg

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _sortOption = MutableStateFlow("Alphabetically")
    val sortOption: StateFlow<String> get() = _sortOption

    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> get() = _darkMode

    val sortOptions = listOf("Alphabetically", "Priority", "Due Date", "Task Difficulty")

    private val db = AppDatabase.getDatabase(app)
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    private val api = retrofit.create(TodoApi::class.java)
    private val repo = TodoRepository(api, db.todoDao())

    init {
        fetchTodos()
    }

    fun fetchTodos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = ""
            try {
                var list = repo.fetchTodos()
                list = applySortAndFilter(list)
                _todos.value = list
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMsg.value = "Error loading tasks: ${e.message}"
            }
            _isLoading.value = false
        }
    }

    fun addTodo(todo: com.example.doit.model.TodoEntity) {
        viewModelScope.launch {
            repo.addTodo(todo)
            _todos.value = applySortAndFilter(_todos.value + todo)
        }
    }

    fun removeTodo(todo: com.example.doit.model.TodoEntity) {
        viewModelScope.launch {
            repo.removeTodo(todo)
            _todos.value = applySortAndFilter(_todos.value - todo)
        }
    }

    fun setSearch(query: String) {
        _searchQuery.value = query
        viewModelScope.launch { _todos.value = applySortAndFilter(repo.fetchTodos()) }
    }

    fun setSort(option: String) {
        _sortOption.value = option
        viewModelScope.launch { _todos.value = applySortAndFilter(repo.fetchTodos()) }
    }

    private fun applySortAndFilter(list: List<com.example.doit.model.TodoEntity>): List<com.example.doit.model.TodoEntity> {
        var filtered = list.filter { it.title.contains(_searchQuery.value, ignoreCase = true) }
        filtered = when(_sortOption.value) {
            "Alphabetically" -> filtered.sortedBy { it.title }
            "Priority" -> filtered.sortedByDescending { it.priority }
            "Due Date" -> filtered.sortedBy { it.dueDate }
            "Task Difficulty" -> filtered.sortedBy { it.difficulty }
            else -> filtered
        }
        return filtered
    }

    fun toggleDarkMode() {
        _darkMode.value = !_darkMode.value
    }
}
