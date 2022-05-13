package com.rohit.todo.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rohit.todo.models.Todo

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().uid

    private val _todoList = MutableLiveData<List<Todo>>()
    val todoList: LiveData<List<Todo>>
        get() = _todoList

    init {
        getTodo()
    }

    fun getTodo() {
        val data = mutableListOf<Todo>()
        db.collection("todos").document(uid.toString()).collection("todo").get()
            .addOnSuccessListener {
                for (document in it){
                    val todo: Todo = document.toObject(Todo::class.java)
                    data.add(todo)
                }
                _todoList.value = data
                Log.i(TAG, "getTodo: $data")
            }
    }

    fun addTodo(body: String) {
        db.collection("todos").document(uid.toString()).collection("todo")
            .add(Todo(body, false))
            .addOnCompleteListener {
                Log.d(TAG, "addTodo: completed successfully ")
            }
            .addOnFailureListener {
                Log.d(TAG, "addTodo: Failed ")
            }
    }

    fun updateTodo(todo_id: String, body: String){
        db.collection("todos").document(uid.toString()).collection("todo").document(todo_id)
    }
}