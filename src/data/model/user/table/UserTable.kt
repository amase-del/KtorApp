package com.example.data.model.user.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val userId = varchar("idUser", 512)
    val email = varchar("email", 512)
    val hashPassword = varchar("hashPassword", 512)
    val name = varchar("name", 512)
    val surname = varchar("surname", 512)

    override val primaryKey: PrimaryKey = PrimaryKey(userId)
}