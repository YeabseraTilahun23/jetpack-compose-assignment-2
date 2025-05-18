package com.example.doit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean,
    val dueDate: Long,
    val difficulty: String,
    val priority: Int,
    val subtasks: List<String>
)
