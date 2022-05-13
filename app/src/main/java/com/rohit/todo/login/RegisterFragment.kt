package com.rohit.todo.login

import android.app.ProgressDialog
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
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        auth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            if (binding.registerEmail.text.isNotEmpty() && binding.registerPassword.text.isNotEmpty()){
                progressDialog = ProgressDialog(context)
                progressDialog.setCancelable(false)
                progressDialog.setMessage("Creating user, please wait...")
                progressDialog.show()
                registerUser()
            }else{
                Toast.makeText(context,"Please Enter all fields!",Toast.LENGTH_SHORT).show()
            }

        }
        return binding.root
    }

    private fun registerUser() {
        auth.createUserWithEmailAndPassword(binding.registerEmail.text.toString(),binding.registerPassword.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.cancel()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT).show()
                }
                else
                    progressDialog.cancel()
                    Toast.makeText(context,"Error registering in!", Toast.LENGTH_SHORT).show()

            }
    }
}