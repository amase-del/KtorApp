package com.example.data.model

import io.ktor.routing.*

data class SimpleResponse(
    val success: Boolean,
    val message: String
)
