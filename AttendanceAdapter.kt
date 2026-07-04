package com.example.studentattandance

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentattandance.model.Attendance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AttendanceAdapter(
    private val context: Context,
    private val attendanceList: ArrayList<Attendance>
) : RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)

        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.attendance_item, parent, false)

        return AttendanceViewHolder(view)

    }

    override fun getItemCount(): Int {

        return attendanceList.size

    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {

        val attendance = attendanceList[position]

        holder.txtDate.text = "Date : ${attendance.date}"
        holder.txtStatus.text = "Status : ${attendance.status}"

        // Update Attendance
        holder.btnEdit.setOnClickListener {

            val intent = Intent(context, UpdateAttendanceActivity::class.java)

            intent.putExtra("attendanceId", attendance.id)
            intent.putExtra("attendanceDate", attendance.date)
            intent.putExtra("attendanceStatus", attendance.status)

            context.startActivity(intent)

        }

        // Delete Attendance
        holder.btnDelete.setOnClickListener {

            FirebaseDatabase.getInstance()
                .reference
                .child("Attendance")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child(attendance.id)
                .removeValue()

        }

    }

}