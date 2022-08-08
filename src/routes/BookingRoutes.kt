package com.example.routes

import com.example.data.model.SimpleResponse
import com.example.data.model.booking.Booking
import com.example.data.model.user.User
import com.example.repository.repoBooking.RepoBooking
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val BOOKING = "$API_VERSION/booking"
const val CREATE_BOOKING = "$BOOKING/create"
const val UPDATE_BOOKING = "$BOOKING/update"
const val DELETE_BOOKING = "$BOOKING/delete"

@Location(CREATE_BOOKING)
class BookingCreateRoute

@Location(UPDATE_BOOKING)
class BookingUpdateRoute

@Location(DELETE_BOOKING)
class BookingDeleteRoute

@Location(BOOKING)
class BookingGetRoute

fun Route.BookingRoutes(
    db: RepoBooking,
    hashFunction: (String) -> String
) {
    authenticate("jwt") {
        post<BookingCreateRoute> {
            val booking = try {
                call.receive<Booking>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing fields"))
                return@post
            }

            try {
                val id = call.principal<User>()!!.userId
                db.addBooking(booking, id)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Booking added"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem"))
            }
        }


        get<BookingGetRoute>{

            try {
                val idUser = call.principal<User>()!!.userId
                val booking = db.getAllBooking(idUser)
                call.respond(HttpStatusCode.OK, booking)
            } catch (e:Exception){

                call.respond(HttpStatusCode.Conflict, emptyList<Booking>())
            }
        }



        post<BookingUpdateRoute> {

            val note = try {
                call.receive<Booking>()
            } catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing Fields"))
                return@post
            }

            try {

                val id = call.principal<User>()!!.userId
                db.updateBooking(note,id)
                call.respond(HttpStatusCode.OK,SimpleResponse(true,"Booking Updated Successfully!"))

            } catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message ?: "Some Problem Occurred!"))
            }

        }


        delete<BookingDeleteRoute> {

            val bookingId = try{
                call.request.queryParameters["id"]!!
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"QueryParameter:id is not present"))
                return@delete
            }


            try {

                val id = call.principal<User>()!!.userId
                db.deleteBooking(bookingId,id)
                call.respond(HttpStatusCode.OK,SimpleResponse(true,"Booking Deleted Successfully!"))

            } catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false, e.message ?: "Some problem Occurred!"))
            }

        }



    }
}

