package br.com.myfitt.treinos.data.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let {
            LocalDateTime.parse(it)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString() // Salva no formato ISO-8601 (ex: 2023-10-05T14:30:00)
    }
}