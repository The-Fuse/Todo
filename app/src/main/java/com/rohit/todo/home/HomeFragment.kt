package com.rohit.todo.home

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.rohit.todo.R
import com.rohit.todo.databinding.DialogTodoBinding
import com.rohit.todo.databinding.FragmentHomeBinding
import com.rohit.todo.login.LoginActivity
import com.rohit.todo.models.Status
import com.rohit.todo.models.Todo


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.addTodo.setOnClickListener {
            viewModel.addTodo("Grow assignment")
        }
        binding.lifecycleOwner = this
        val adapter = TodoRecyclerAdapter()
        binding.todoRecyclerView.adapter = adapter
        viewModel.todoList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Please wait...")

        binding.addTodo.setOnClickListener {
            showDialog()
        }

        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        initializeClickListener(adapter)

        viewModel.addTodoStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == Status.Success || it == Status.Error)
                    progressDialog.cancel()
            }
        })

        viewModel.updateTodoStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == Status.Success || it == Status.Error)
                    progressDialog.cancel()
            }
        })

        viewModel.deleteTodoStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == Status.Success || it == Status.Error)
                    progressDialog.cancel()
            }
        })

        viewModel.getTodoStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == Status.Success || it == Status.Error)
                    binding.progressBar.visibility = View.GONE
            }
        })

        return binding.root
    }

    private fun initializeClickListener(adapter: TodoRecyclerAdapter) {
        adapter.todoClickListener =
            object : TodoRecyclerAdapter.TodoClickListener {

                override fun onTodoSelected(todo: Todo, layout: ConstraintLayout) {
                    showDialog(todo)
                }

            }
        adapter.deleteClickListener =
            object : TodoRecyclerAdapter.DeleteClickListener {
                override fun onDeleteSelected(todo: Todo, imageView: ImageView) {
                    progressDialog.show()
                    viewModel.deleteTodo(todo)
                    viewModel.getTodo()
                }

            }
        adapter.checkClickListener =
            object : TodoRecyclerAdapter.CheckClickListener {
                override fun onCheckSelected(todo: Todo, checkBox: CheckBox) {
                    viewModel.updateStatus(todo.id,todo.status)
                    viewModel.getTodo()
                }

            }
    }

    fun showDialog(todo: Todo? = null) {

        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        val dialogBinding = DialogTodoBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        var et: EditText? = null
        val window: Window? = dialog.getWindow()
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        if (todo!=null){
            dialogBinding.et.setText(todo.body)
            dialogBinding.addTodoButton.text = "Update"
        }

        val btnok = dialog.findViewById(R.id.add_todo_button) as Button
        btnok.setOnClickListener {
            if (todo==null){
                if (!dialogBinding.et.text.isEmpty()){
                    progressDialog.show()
                    viewModel.addTodo(dialogBinding.et.text.toString())
                    viewModel.getTodo()
                }

            }else{
                progressDialog.show()
                viewModel.updateTodo(todo.id, dialogBinding.et.text.toString())
                viewModel.getTodo()
            }

            dialog.dismiss()
        }


        dialogBinding.closeDialog.setOnClickListener { dialog.dismiss() }

        dialog.show()

    }

}