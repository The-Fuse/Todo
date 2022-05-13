package com.rohit.todo.models

data class Todo(

    val body: String = "",
    val status: Boolean = false,
    val timeStamp: String = System.currentTimeMillis().toString(),
    val id: String = System.currentTimeMillis().toString(),
)