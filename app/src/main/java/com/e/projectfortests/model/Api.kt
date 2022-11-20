package com.e.projectfortests.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class Api {
    private val service = HttpClient.jsonplaceholderService

    suspend fun fetchToDo() = coroutineScope {
        val response = withContext(Dispatchers.IO) { service.getTODO() }

        if (response.isSuccessful) return@coroutineScope response.body()
        else throw HttpException(response)
    }
    suspend fun fetchUsers() = coroutineScope {
        val response = withContext(Dispatchers.IO) { service.getUsers() }

        if (response.isSuccessful) return@coroutineScope response.body()
        else throw HttpException(response)
    }
}