package com.wanderlust.domain.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date

object DateFormatter {
    private val dateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

    fun getCurrentDateString(): String = dateFormat.format(LocalDateTime.now())

    fun convertToDate(stringDate: String): Date = dateFormat.parse(stringDate)
}