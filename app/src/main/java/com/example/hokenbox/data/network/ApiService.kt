package com.example.hokenbox.data.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

typealias Parameters = HashMap<String, Any>
typealias Headers = HashMap<String, String>

interface ApiService : BaseApiService

interface ApiServiceNoLogin : BaseApiService

interface RefreshTokenApiService : BaseApiService

interface BaseApiService {

    @GET
    suspend fun get(
        @Url url: String,
        @HeaderMap headers: Headers = hashMapOf(),
        @QueryMap parameters: Parameters = hashMapOf()
    ): ResponseBody

    @POST
    suspend fun post(
        @Url url: String,
        @HeaderMap headers: Headers = hashMapOf(),
        @Body parameters: Parameters = hashMapOf()
    ): ResponseBody

    @PUT
    suspend fun put(
        @Url url: String,
        @HeaderMap headers: Headers = hashMapOf(),
        @Body parameters: Parameters = hashMapOf()
    ): ResponseBody

    @DELETE
    suspend fun delete(
        @Url url: String,
        @HeaderMap headers: Headers = hashMapOf(),
        @QueryMap parameters: Parameters = hashMapOf()
    ): ResponseBody

    @Multipart
    @POST
    suspend fun uploadImage(
        @Url url: String,
        @HeaderMap headers: Headers = hashMapOf(),
        @Part image: MultipartBody.Part? = null
    ): ResponseBody
}
