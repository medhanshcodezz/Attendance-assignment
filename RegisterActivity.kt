package com.example.studentattandance

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    lateinit var name: EditText

    lateinit var email: EditText

    lateinit var password: EditText

    lateinit var register: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        name = findViewById(R.id.etName)

        email = findViewById(R.id.etEmail)

        password = findViewById(R.id.etPassword)

        register = findViewById(R.id.btnRegister)

        register.setOnClickListener {

            val studentName = name.text.toString()

            val studentEmail = email.text.toString()

            val studentPassword = password.text.toString()

            auth.createUserWithEmailAndPassword(studentEmail, studentPassword)

                .addOnCompleteListener {

                    if(it.isSuccessful){

                        val uid = auth.currentUser!!.uid

                        FirebaseDatabase.getInstance().reference

                            .child("Students")

                            .child(uid)

                            .child("name")

                            .setValue(studentName)

                        Toast.makeText(this,"Registration Successful",Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, LoginActivity::class.java))

                        finish()

                    }

                    else{

                        Toast.makeText(this,it.exception!!.message,Toast.LENGTH_SHORT).show()

                    }

                }

        }

    }

}