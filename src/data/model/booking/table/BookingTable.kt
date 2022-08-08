package com.example.data.model.booking.table

import com.example.data.model.user.table.UserTable
import org.jetbrains.exposed.sql.Table

object BookingTable: Table() {
    val bookingId = varchar("bookingId", 512)
    val userId = varchar("userId", 512).references(UserTable.userId)
    val timeStart = long("timeStart")
    val timeEnd = long("timeEnd")

    override val primaryKey: PrimaryKey = PrimaryKey(bookingId)

}