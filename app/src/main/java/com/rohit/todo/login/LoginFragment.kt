package com.rohit.todo.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rohit.todo.MainActivity
import com.rohit.todo.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            loginByEmail()
        }
        binding.registerButton.setOnClickListener {
            this.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragment2ToRegisterFragment())
        }

        return binding.root
    }

    private fun loginByEmail() {
        auth.signInWithEmailAndPassword(
            binding.loginEmail.text.toString(),
            binding.loginPassword.text.toString()
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Logged In successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                }
                else
                    Toast.makeText(context, "Error Logging in!", Toast.LENGTH_SHORT).show()

            }
    }

}