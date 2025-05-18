package com.example.doit.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromSubtasks(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split("||")
    }

    @TypeConverter
    fun subtasksToString(list: List<String>): String {
        return list.joinToString("||")
    }
}
