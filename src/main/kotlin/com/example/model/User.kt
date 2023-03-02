package com.example.model

data class User(
    val id : String,
    val firstName : String,
    val lastName : String
)

data class LoginInformation(
    val email : String,
    val password : String
)