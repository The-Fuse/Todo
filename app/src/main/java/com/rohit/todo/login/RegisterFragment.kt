package com.rohit.todo.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.rohit.todo.MainActivity
import com.rohit.todo.R
import com.rohit.todo.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {
   private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        auth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            registerUser()
        }
        return binding.root
    }

    private fun registerUser() {
        auth.createUserWithEmailAndPassword(binding.registerEmail.text.toString(),binding.registerPassword.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(context,"Error registering in!", Toast.LENGTH_SHORT).show()

            }
    }
}