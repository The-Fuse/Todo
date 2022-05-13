package com.rohit.todo.home

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.rohit.todo.MainActivity
import com.rohit.todo.R
import com.rohit.todo.databinding.FragmentHomeBinding
import com.rohit.todo.login.LoginActivity


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

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
        binding.addTodo.setOnClickListener {
            showDialog(this.requireActivity())
        }

        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    fun showDialog(activity: Activity) {

        val dialog = Dialog(activity)
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_todo)
        var et: EditText? = null
        val window: Window? = dialog.getWindow()
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        et = dialog.findViewById(R.id.et)


        val btnok = dialog.findViewById(R.id.add_todo_button) as Button
        btnok.setOnClickListener {
            viewModel.addTodo(et.text.toString())
            viewModel.getTodo()
            dialog.dismiss()
        }

        val close_dialog = dialog.findViewById(R.id.close_dialog) as Button
        close_dialog.setOnClickListener { dialog.dismiss() }

        dialog.show()

    }

}