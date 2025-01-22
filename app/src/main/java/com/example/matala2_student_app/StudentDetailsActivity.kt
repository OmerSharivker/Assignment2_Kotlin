package com.example.matala2_student_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.matala2_student_app.model.Model
import com.google.android.material.floatingactionbutton.FloatingActionButton


class StudentDetailsActivity : AppCompatActivity() {

    //private lateinit var nameTextView: TextView
    //private lateinit var idTextView: TextView
    //private lateinit var phoneTextView: TextView
    //private lateinit var addressTextView: TextView
    //private lateinit var checkBox: CheckBox
    private lateinit var editStudentLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        //Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_title_student_details)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.toolbar_title_student_details)


        // Initialize UI components
        val nameTextView: TextView = findViewById(R.id.student_details_activity_name_text_view)
        val idTextView: TextView = findViewById(R.id.student_details_activity_id_text_view)
        val phoneTextView: TextView = findViewById(R.id.student_details_activity_phone_text_view)
        val addressTextView: TextView = findViewById(R.id.student_details_activity_address_text_view)
        val checkBox: CheckBox = findViewById(R.id.student_details_activity_checkbox)
        val editButton: Button = findViewById(R.id.student_details_activity_edit_button)
        val backButton: Button = findViewById(R.id.student_details_activity_back_button)


        // Get student details from the intent
        val studentName = intent.getStringExtra("STUDENT_NAME") ?: ""
        val studentId = intent.getStringExtra("STUDENT_ID") ?: ""
        val studentPhone = intent.getStringExtra("STUDENT_PHONE") ?: ""
        val studentAddress = intent.getStringExtra("STUDENT_ADDRESS") ?: ""
        val studentIsChecked = intent.getBooleanExtra("STUDENT_IS_CHECKED", false)

        // Installize  the data from StudentRecyclerView activity to the UI
        nameTextView.text = studentName
        idTextView.text =  studentId
        phoneTextView.text =  studentPhone
        addressTextView.text = studentAddress
        checkBox.isChecked = studentIsChecked

        // editStudentLauncher is an ActivityResultLauncher that launches the EditStudentActivity
        editStudentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {

                // Get the updated student details from the intent  from EditStudentActivity
                val updatedId = result.data!!.getStringExtra("STUDENT_ID")
                val originalId = result.data!!.getStringExtra("STUDENT_ORIGINAL_ID")
                val updatedName = result.data!!.getStringExtra("STUDENT_NAME")
                val updatedPhone = result.data!!.getStringExtra("STUDENT_PHONE")
                val updatedAddress = result.data!!.getStringExtra("STUDENT_ADDRESS")
                val updatedIsChecked = result.data!!.getBooleanExtra("STUDENT_IS_CHECKED", false)

                Log.d("StudentDetailsActivity", "Stduent Original ID: $originalId")
                Log.d("StudentDetailsActivity", "Updated student details sent back with ID: $updatedId")
                Log.d("StudentDetailsActivity", "Updated student details sent back with Name: $updatedName")
                Log.d("StudentDetailsActivity", "Updated student details sent back with Phone: $updatedPhone")
                Log.d("StudentDetailsActivity", "Updated student details sent back with Address: $updatedAddress")
                Log.d("StudentDetailsActivity", "Updated student details sent back with IsChecked: $updatedIsChecked")

                // Update the UI components with the new data
                findViewById<TextView>(R.id.student_details_activity_name_text_view).text = updatedName
                findViewById<TextView>(R.id.student_details_activity_id_text_view).text = updatedId
                findViewById<TextView>(R.id.student_details_activity_phone_text_view).text = updatedPhone
                findViewById<TextView>(R.id.student_details_activity_address_text_view).text = updatedAddress
                findViewById<CheckBox>(R.id.student_details_activity_checkbox).isChecked = updatedIsChecked

                intent.putExtras(result.data!!) // Send the updated student details back to the StudentRecyclerView activity
                setResult(RESULT_OK, intent) // Set the result code to RESULT_OK
            }
        }
        //Set Listeners
        editButton.setOnClickListener {
            val intent = Intent(this, EditStudentActivity::class.java).apply {
                putExtra("STUDENT_ID", studentId)
                putExtra("STUDENT_NAME", studentName)
                putExtra("STUDENT_PHONE", studentPhone)
                putExtra("STUDENT_ADDRESS", studentAddress)
                putExtra("STUDENT_IS_CHECKED", studentIsChecked)


            }
            editStudentLauncher.launch(intent)

        }
        backButton.setOnClickListener{
            finish()
        }
    }

}