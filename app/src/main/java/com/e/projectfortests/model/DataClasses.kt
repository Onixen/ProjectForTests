package com.e.projectfortests.model

data class ToDo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)
data class User(
    val id: Int,
    val name: String
)

data class HomeScreenRecyclerItem(
    val name: String,
    val job: String,
    val completed: Boolean
)