package com.example.repository.repoBooking

import com.example.data.model.booking.Booking
import com.example.data.model.booking.table.BookingTable
import com.example.repository.DatabaseFactory
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class RepoBooking {

    suspend fun addBooking(booking: Booking, userId: String) {
        DatabaseFactory.dbQuery {
            BookingTable.insert { bt ->
                bt[BookingTable.bookingId] = booking.bookingId
                bt[BookingTable.userId] = userId
                bt[BookingTable.timeStart] = booking.timeStart
                bt[BookingTable.timeEnd] = booking.timeEnd
            }
        }
    }

    suspend fun getAllBooking(userId: String): List<Booking> = dbQuery {

        BookingTable.select {
            BookingTable.userId.eq(userId)
        }.mapNotNull { rowToBooking(it) }

    }

    suspend fun updateBooking(booking: Booking, userId: String) {

        dbQuery {

            BookingTable.update(
                where = {
                    BookingTable.userId.eq(userId) and BookingTable.bookingId.eq(booking.bookingId)
                }
            ) { bt ->
                bt[timeStart] = booking.timeStart
                bt[timeEnd] = booking.timeEnd
            }
        }
    }

    suspend fun deleteBooking(idBooking: String, userId: String) {
        dbQuery {
            BookingTable.deleteWhere { BookingTable.bookingId.eq(idBooking) and BookingTable.userId.eq(userId) }
        }
    }

    private fun rowToBooking(row: ResultRow?): Booking? {
        if (row == null) {
            return null
        }

        return Booking(
            bookingId = row[BookingTable.bookingId],
            timeStart = row[BookingTable.timeStart],
            timeEnd = row[BookingTable.timeEnd]
        )
    }
}