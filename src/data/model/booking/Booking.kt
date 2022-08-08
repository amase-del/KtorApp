package com.example.data.model.booking

import io.ktor.auth.*

data class Booking(
    val bookingId: String,
    val timeStart: Long,
    val timeEnd: Long
)