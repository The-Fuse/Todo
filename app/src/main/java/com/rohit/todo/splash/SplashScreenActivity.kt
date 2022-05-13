package com.rohit.todo.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.rohit.todo.MainActivity
import com.rohit.todo.R
import com.rohit.todo.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser!=null){
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
            finish()
        }else{
            val myIntent = Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
            finish()
        }
    }
}