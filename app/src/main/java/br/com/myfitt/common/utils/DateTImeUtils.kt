package br.com.myfitt.common.utils

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.abs

fun LocalDateTime.epochMilli(): Long {
    return instant().toEpochMilli()
}
fun LocalDateTime.printDate(): String {
    return print("dd/MM/yyyy")
}

fun LocalDateTime.printSimpleDate(): String {
    return print("dd/MM")
}
fun LocalDateTime.printSimpleTime(): String {
    return print("hh\\mmm\\s")
}

fun LocalDateTime.printTime(): String {
    return print("hh:mm:ss")
}

fun LocalDateTime.print(pattern: String = "dd/MM/yyyy hh:mm:ss"): String {
    return runCatching { DateTimeFormatter.ofPattern(pattern).format(this) }.getOrNull() ?: "Erro!"
}

fun LocalDateTime.instant(): Instant {
    return toInstant(ZoneOffset.ofHours(-3))
}

fun differenceSeconds(date1: LocalDateTime, date2: LocalDateTime): Int {
    return abs(Duration.between(date1, date2).seconds.toInt())
}