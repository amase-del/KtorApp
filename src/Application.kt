package com.example

import com.example.authentication.JwtService
import com.example.authentication.hash
import com.example.repository.DatabaseFactory
import com.example.repository.repoBooking.RepoBooking
import com.example.repository.repoUser.RepoUser
import com.example.routes.BookingRoutes
import com.example.routes.UserRoutes
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.locations.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    DatabaseFactory.init()
    val dbUser = RepoUser()
    val dbBooking = RepoBooking()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(Authentication) {

        jwt ("jwt")  {
            verifier(jwtService.verifier)
            realm = "Booking Server"
            validate {
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = dbUser.findUserByEmail(email)
                user
            }
        }

    }

    install(Locations) {

    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {

        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        UserRoutes(dbUser, jwtService, hashFunction)
        BookingRoutes(dbBooking,hashFunction)

        route("/notes") {

            route("/create") {
                // localhost:8081/notes/create
                post {
                    val body = call.receive<String>()
                    call.respond(body)
                }
            }

            delete {
                val body = call.receive<String>()
                call.respond(body)
            }
        }


    }
}

data class MySession(val count: Int = 0)

