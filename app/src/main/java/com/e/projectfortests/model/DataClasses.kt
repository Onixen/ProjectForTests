package com.e.projectfortests.model

class TODOs: ArrayList<ToDo>()
class Users: ArrayList<User>()

data class ToDo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: UserAddress
)
data class UserAddress(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: UserGeo
)
data class UserGeo(
    var lat: String,
    var lng: String
)
data class HomeScreenRecyclerItem(
    val name: String,
    val job: String,
    val completed: Boolean
)