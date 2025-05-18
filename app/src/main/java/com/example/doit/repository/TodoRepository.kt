package com.example.doit.repository

import com.example.doit.dao.TodoDao
import com.example.doit.model.TodoEntity
import com.example.doit.network.TodoApi
import retrofit2.HttpException

class TodoRepository(private val api: TodoApi, private val dao: TodoDao) {

    suspend fun fetchTodos(): List<TodoEntity> {
        return try {
            val response = api.getTodos()
            if (response.isSuccessful) {
                val todos = response.body() ?: emptyList()
                val mappedTodos = todos.map {
                    TodoEntity(it.id, it.userId, it.title, it.completed, it.dueDate, it.difficulty, it.priority, it.subtasks)
                }
                dao.deleteAllTodos()
                dao.insertTodos(mappedTodos)
                mappedTodos
            } else {
                val localTodos = dao.getAllTodos()
                if (localTodos.isEmpty()) emptyList() else localTodos
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val localTodos = dao.getAllTodos()
            if (localTodos.isEmpty()) emptyList() else localTodos
        }
    }

    suspend fun addTodo(todo: TodoEntity) {
        try {
            dao.insertTodo(todo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeTodo(todo: TodoEntity) {
        try {
            dao.deleteTodo(todo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


