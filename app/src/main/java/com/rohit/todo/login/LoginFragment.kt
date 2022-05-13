package com.rohit.todo.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rohit.todo.MainActivity
import com.rohit.todo.R
import com.rohit.todo.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            progressDialog = ProgressDialog(context)
            progressDialog.setCancelable(false)
            progressDialog.setMessage("Logging in, please wait...")
            progressDialog.show()
            loginByEmail()
        }
        binding.registerButton.setOnClickListener {
            this.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragment2ToRegisterFragment())
        }

        binding.googleLoginButton.setOnClickListener {
            // TODO: Login with google
            loginByGoogle()
        }

        return binding.root
    }

    private fun loginByGoogle() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)

        auth = FirebaseAuth.getInstance()

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun loginByEmail() {
        auth.signInWithEmailAndPassword(
            binding.loginEmail.text.toString(),
            binding.loginPassword.text.toString()
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.cancel()
                    Toast.makeText(context, "Logged In successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                else
                    Toast.makeText(context, "Error Logging in!", Toast.LENGTH_SHORT).show()

            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!


                Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show()
                firebaseAuthWithGoogle(account.idToken!!)



            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(context, "Failed to Sign in !!", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(context, "Successfully Logged in", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Error Logging in", Toast.LENGTH_SHORT).show()
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }


}