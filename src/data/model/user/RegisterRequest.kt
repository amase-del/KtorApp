package com.example.data.model.user

import com.sun.xml.internal.ws.api.ha.StickyFeature

data class RegisterRequest(
    var idUser: String,
    val email: String,
    val password: String,
    val name: String,
    val surname: String
)
