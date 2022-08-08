package com.example.data.model.user

import io.ktor.auth.*

data class User(
    val userId: String,
    val email: String,
    val hashPassword: String,
    val userName: String,
    val surname: String
): Principal
