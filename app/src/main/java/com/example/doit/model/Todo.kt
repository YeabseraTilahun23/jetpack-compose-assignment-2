package com.example.doit.model

data class Todo(
    val id: Int,
    val userId: Int = 0,
    val title: String,
    val completed: Boolean,
    val dueDate: Long = System.currentTimeMillis(),
    val difficulty: String = "Medium",
    val priority: Int = 3,
    val subtasks: List<String> = emptyList()
)


fun Todo.toEntity(): TodoEntity {
    return TodoEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        completed = this.completed,
        dueDate = this.dueDate,
        difficulty = this.difficulty,
        priority = this.priority,
        subtasks = this.subtasks
    )
}
