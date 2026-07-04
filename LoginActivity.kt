package com.example.studentattandance

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.etEmail)

        val password = findViewById<EditText>(R.id.etPassword)

        val login = findViewById<Button>(R.id.btnLogin)

        val register = findViewById<Button>(R.id.btnRegister)

        login.setOnClickListener {

            auth.signInWithEmailAndPassword(

                email.text.toString(),

                password.text.toString()

            ).addOnCompleteListener {

                if(it.isSuccessful){

                    startActivity(Intent(this,DashboardActivity::class.java))

                    finish()

                }

                else{

                    Toast.makeText(this,it.exception!!.message,Toast.LENGTH_SHORT).show()

                }

            }

        }

        register.setOnClickListener {

            startActivity(Intent(this,RegisterActivity::class.java))

        }

    }

}