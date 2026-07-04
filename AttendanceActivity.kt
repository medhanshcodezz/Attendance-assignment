package com.example.studentattandance

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentattandance.model.Attendance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AttendanceActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var attendanceList: ArrayList<Attendance>
    private lateinit var attendanceAdapter: AttendanceAdapter

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var radioGroup: RadioGroup
    private lateinit var btnMarkAttendance: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
            .reference
            .child("Attendance")
            .child(auth.currentUser!!.uid)

        // Initialize Views
        radioGroup = findViewById(R.id.radioGroupAttendance)
        btnMarkAttendance = findViewById(R.id.btnMarkAttendance)
        recyclerView = findViewById(R.id.recyclerAttendance)

        // RecyclerView Setup
        attendanceList = ArrayList()

        attendanceAdapter = AttendanceAdapter(this, attendanceList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = attendanceAdapter

        // Load existing attendance records
        loadAttendance()

        // Mark Attendance Button
        btnMarkAttendance.setOnClickListener {

            val selectedId = radioGroup.checkedRadioButtonId

            if (selectedId == -1) {
                Toast.makeText(
                    this,
                    "Please select Present or Absent",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val selectedRadioButton =
                findViewById<RadioButton>(selectedId)

            val attendanceStatus =
                selectedRadioButton.text.toString()

            val attendanceId = database.push().key!!

            val currentDate =
                java.text.SimpleDateFormat(
                    "dd-MM-yyyy",
                    java.util.Locale.getDefault()
                ).format(java.util.Date())

            val attendance = Attendance(
                attendanceId,
                currentDate,
                attendanceStatus
            )

            database.child(attendanceId)
                .setValue(attendance)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Attendance Marked Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    radioGroup.clearCheck()

                }
                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        "Failed : ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                }

        }

    }

    private fun loadAttendance() {

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                attendanceList.clear()

                if (snapshot.exists()) {

                    for (attendanceSnapshot in snapshot.children) {

                        val attendance =
                            attendanceSnapshot.getValue(Attendance::class.java)

                        if (attendance != null) {

                            attendanceList.add(attendance)

                        }

                    }

                }

                attendanceAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(
                    this@AttendanceActivity,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()

            }

        })

    }

}