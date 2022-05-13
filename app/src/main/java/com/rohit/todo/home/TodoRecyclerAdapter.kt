package com.rohit.todo.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rohit.todo.databinding.TodoItemBinding
import com.rohit.todo.models.Todo

class TodoRecyclerAdapter: ListAdapter<Todo,TodoRecyclerAdapter.ViewHolder>(TodoDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Todo
        ) {
            binding.todoContent.setOnClickListener {
                todoClickListener.onTodoSelected(item,binding.todoContent)
            }
            binding.deleteTodo.setOnClickListener {
                deleteClickListener.onDeleteSelected(item,binding.deleteTodo)
            }
            binding.checkBox.setOnClickListener {
                checkClickListener.onCheckSelected(item,binding.checkBox)
            }
            binding.todoBody.text = item.body.toString()
            binding.checkBox.isChecked = item.status
        }

    }

    interface TodoClickListener {

        fun onTodoSelected(todo: Todo, layout: ConstraintLayout)
    }
    interface DeleteClickListener {
        fun onDeleteSelected(todo: Todo, imageView: ImageView)
    }

    interface CheckClickListener {
        fun onCheckSelected(todo: Todo, checkBox: CheckBox)
    }
    lateinit var todoClickListener: TodoClickListener
    lateinit var deleteClickListener: DeleteClickListener
    lateinit var checkClickListener: CheckClickListener

}

class TodoDiffCallBack : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id ==    newItem.id
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }


}
