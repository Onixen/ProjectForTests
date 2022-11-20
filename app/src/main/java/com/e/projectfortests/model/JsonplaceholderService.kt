package com.e.projectfortests.model

import retrofit2.Response
import retrofit2.http.GET

interface JsonplaceholderService {
    @GET("todos")
    suspend fun getTODO(): Response<TODOs>

    @GET("users")
    suspend fun getUsers(): Response<Users>
}