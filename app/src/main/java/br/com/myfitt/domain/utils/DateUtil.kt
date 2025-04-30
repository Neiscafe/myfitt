package br.com.myfitt.domain.utils;

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtil {
    private val dateTimeFormater =
        DateTimeFormatter.ofPattern("dd/MM").withZone(ZoneId.systemDefault())
    private val dbFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault())
    val now get() = LocalDate.now()
    fun format(date: LocalDate): String {
        return date.format(dateTimeFormater)
    }

    fun fromMillisUtcToLocal(millis: Long): LocalDate {
        return LocalDateTime.from(Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC"))).atZone(
            ZoneId.systemDefault()
        ).toLocalDate()
    }

    fun fromDbNotation(date: String): LocalDate {
        return LocalDate.parse(date, dbFormatter)
    }

    fun toDbNotation(date: LocalDate): String {
        return date.format(dbFormatter)
    }

    fun toMillis(date: LocalDate): Long {
        return Instant.from(date.atStartOfDay().atZone(ZoneId.systemDefault())).toEpochMilli()
    }

    fun toMillisUtcFromLocal(date: LocalDate): Long {
        return Instant.from(date.atStartOfDay().atZone(ZoneId.of("UTC"))).toEpochMilli()
    }
}