package com.e.projectfortests.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

class Api {
    private val service = HttpClient.jsonplaceholderService

    /**
     * @return List<[ToDo]> - список задач
     * */
    suspend fun fetchToDo() = coroutineScope {
        val response = withContext(Dispatchers.IO) {
            service.getTODO()
        }
        if (response.isSuccessful) {
            return@coroutineScope response.body()
        } else {
            throw HttpException(response)
        }
    }
}