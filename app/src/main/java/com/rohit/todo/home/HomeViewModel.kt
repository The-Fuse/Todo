package com.rohit.todo.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rohit.todo.models.Status
import com.rohit.todo.models.Todo

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().uid

    private val _addTodoStatus = MutableLiveData<Status>()
    val addTodoStatus: LiveData<Status>
        get() = _addTodoStatus

    private val _updateTodoStatus = MutableLiveData<Status>()
    val updateTodoStatus: LiveData<Status>
        get() = _updateTodoStatus

    private val _deleteTodoStatus = MutableLiveData<Status>()
    val deleteTodoStatus: LiveData<Status>
        get() = _deleteTodoStatus

    private val _getTodoStatus = MutableLiveData<Status>()
    val getTodoStatus: LiveData<Status>
        get() = _getTodoStatus

    private val _todoList = MutableLiveData<List<Todo>>()
    val todoList: LiveData<List<Todo>>
        get() = _todoList

    init {
        getTodo()
    }

    fun getTodo() {
        _getTodoStatus.value = Status.Loading
        val data = mutableListOf<Todo>()
        db.collection("todos").document(uid.toString()).collection("todo").get()
            .addOnSuccessListener {
                _getTodoStatus.value = Status.Success
                for (document in it){
                    val todo: Todo = document.toObject(Todo::class.java)
                    data.add(todo)
                }
                _todoList.value = data
                Log.i(TAG, "getTodo: $data")
            }
            .addOnFailureListener {
                _addTodoStatus.value = Status.Error
            }
    }

    fun addTodo(body: String) {
        _addTodoStatus.value = Status.Loading
        val todo = Todo(body,false)
        db.collection("todos").document(uid.toString()).collection("todo").document(todo.id)
            .set(todo)
            .addOnCompleteListener {
                _addTodoStatus.value = Status.Success
                Log.d(TAG, "addTodo: completed successfully ")
            }
            .addOnFailureListener {
                _addTodoStatus.value = Status.Error
                Log.d(TAG, "addTodo: Failed ")
            }
    }

    fun updateTodo(todo_id: String, body: String){
        _updateTodoStatus.value = Status.Loading
        db.collection("todos").document(uid.toString()).collection("todo").document(todo_id).update("body",body)
            .addOnCompleteListener {
                _updateTodoStatus.value = Status.Success
            }
            .addOnFailureListener {
                _updateTodoStatus.value = Status.Error
            }
    }

    fun updateStatus(todo_id: String,status: Boolean){
        db.collection("todos").document(uid.toString()).collection("todo").document(todo_id).update("status",!status)
    }

    fun deleteTodo(todo: Todo) {
        _deleteTodoStatus.value = Status.Loading
        db.collection("todos").document(uid.toString()).collection("todo").document(todo.id).delete()
            .addOnCompleteListener {
                _deleteTodoStatus.value = Status.Success
            }
            .addOnFailureListener {
                _deleteTodoStatus.value = Status.Error
            }
    }
}
