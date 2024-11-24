package hu.bme.aut.szoftarch.farmgame.api

import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson

const val BACKEND_URL = "http://192.168.68.68:5153/"

abstract class HttpRequestMaker(val token: String) {
    val gson = Gson()
    var client = HttpClient(){
        defaultRequest {
            url(BACKEND_URL)
        }
        install(ContentNegotiation){
            gson()
        }
    }

    suspend fun get(location: String): HttpResponse{
        return client.get(location){
            header("Authorization", token)
        }
    }
    suspend fun post(location: String): HttpResponse {
        return client.post(location){
            header("Authorization", token)
        }
    }
    suspend fun put(location: String): HttpResponse {
        return client.put(location){
            header("Authorization", token)
        }
    }
    suspend fun delete(location: String): HttpResponse {
        return client.delete(location){
            header("Authorization", token)
        }
    }
}