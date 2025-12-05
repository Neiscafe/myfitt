package br.com.myfitt.common.utils

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.abs

fun LocalDateTime.epochMilli(): Long {
    return instant().toEpochMilli()
}

fun LocalDateTime.instant(): Instant {
    return toInstant(ZoneOffset.ofHours(-3))
}
fun differenceSeconds(date1: LocalDateTime, date2: LocalDateTime): Int{
    return abs(Duration.between(date1, date2).seconds.toInt())
}