package com.e.projectfortests.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HttpClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val jsonplaceholderService: JsonplaceholderService = retrofit.create(JsonplaceholderService::class.java)
}