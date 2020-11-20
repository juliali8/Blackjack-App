package com.example.cse438.cse438_assignment4.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cse438.cse438_assignment4.R
import androidx.fragment.app.Fragment
import com.example.cse438.cse438_assignment4.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_signup.*

class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_btn.setOnClickListener {

            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
//                        val firebaseUser = task.result.user
//                        val emailVerified = firebaseUser.isEmailVerified
//                        val user = firebaseUser.toUser()
//                        val providers = firebaseUser.associatedProviders()
//                        //Do something with your data
//                        if (!emailVerified) {} //manage your email verification
                        writeNewUser(email.text.toString())
                    } else {
                        //Manage error
                    }
                }

        }
    }

    private fun writeNewUser(email: String) {
        val uid = FirebaseAuth.getInstance().uid?: ""
        val user = User(uid, email, 1000)
        FirebaseDatabase.getInstance().getReference("/users/$uid").setValue(user)
    }

//    class Users(val uid: String, val email: String, val chips: Int)
}