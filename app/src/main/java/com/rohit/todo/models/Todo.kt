package com.rohit.todo.models

data class Todo(

    val body: String = "",
    val status: Boolean = false,
    val id: String=System.currentTimeMillis().toString(),
)