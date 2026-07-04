package com.example.studentattandance

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studentattandance.model.Attendance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UpdateAttendanceActivity : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var rbPresent: RadioButton
    private lateinit var rbAbsent: RadioButton
    private lateinit var btnUpdate: Button

    private lateinit var attendanceId: String
    private lateinit var attendanceDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_attendance)

        radioGroup = findViewById(R.id.radioGroupUpdate)
        rbPresent = findViewById(R.id.rbPresentUpdate)
        rbAbsent = findViewById(R.id.rbAbsentUpdate)
        btnUpdate = findViewById(R.id.btnUpdate)

        attendanceId = intent.getStringExtra("attendanceId").toString()
        attendanceDate = intent.getStringExtra("attendanceDate").toString()

        val oldStatus = intent.getStringExtra("attendanceStatus")

        if (oldStatus == "Present") {
            rbPresent.isChecked = true
        } else {
            rbAbsent.isChecked = true
        }

        btnUpdate.setOnClickListener {

            val selectedId = radioGroup.checkedRadioButtonId

            if (selectedId == -1) {

                Toast.makeText(
                    this,
                    "Select Attendance",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val status =
                findViewById<RadioButton>(selectedId).text.toString()

            val attendance = Attendance(
                attendanceId,
                attendanceDate,
                status
            )

            FirebaseDatabase.getInstance()
                .reference
                .child("Attendance")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child(attendanceId)
                .setValue(attendance)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Attendance Updated",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()

                }

        }

    }

}