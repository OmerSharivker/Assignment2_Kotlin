package com.example.matala2_student_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.matala2_student_app.model.Model
import com.example.matala2_student_app.model.Student

class EditStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_title_edit_student)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.toolbar_title_edit_student)

        // Initialize UI components
        val saveButton: Button = findViewById(R.id.edit_student_activity_save_button)
        val cancelButton: Button = findViewById(R.id.edit_student_activity_cancel_button)
        val deleteButton: Button = findViewById(R.id.edit_student_activity_delete_button)
        val nameEditText: EditText = findViewById(R.id.edit_student_activity_name_text)
        val idEditText: EditText = findViewById(R.id.edit_student_activity_id_edit_text)
        val phoneEditText: EditText = findViewById(R.id.edit_student_activity_phone_edit_text)
        val addressEditText: EditText = findViewById(R.id.edit_student_activity_address_text)
        val checkBox: CheckBox = findViewById(R.id.edit_student_activity_checkbox)


        // Get the student details from the intent StudentDetailsActivity
        val studentName = intent.getStringExtra("STUDENT_NAME") ?: ""
        val studentId = intent.getStringExtra("STUDENT_ID") ?: ""
        val studentPhone = intent.getStringExtra("STUDENT_PHONE") ?: ""
        val studentAddress = intent.getStringExtra("STUDENT_ADDRESS") ?: ""
        val studentIsChecked = intent.getBooleanExtra("STUDENT_IS_CHECKED", false)
        val originalId = intent.getStringExtra("STUDENT_ID") ?: ""

        // Installize  the data from StudentDetailsActivity to the UI
        nameEditText.setText(studentName)
        idEditText.setText(studentId)
        phoneEditText.setText(studentPhone)
        addressEditText.setText(studentAddress)
        checkBox.isChecked = studentIsChecked

        //-----LISTENERS------
        cancelButton.setOnClickListener{
            finish()
        }

        saveButton.setOnClickListener {
            // ✅ Get the updated student details from the UI
            val updatedName = nameEditText.text.toString()
            val updatedId = idEditText.text.toString()
            val updatedPhone = phoneEditText.text.toString()
            val updatedAddress = addressEditText.text.toString()
            val updatedIsChecked = checkBox.isChecked
            val student = Model.students.find { it.id == originalId }
            if (student != null) {
                // 🔍 Log before the update
                Log.d(
                    "EditStudentActivity",
                    "Before Update: ID=${student.id}, Name=${student.name}, Phone=${student.phone}, Address=${student.address}, Checked=${student.isChecked}"
                )

                //✅ Find the student by the original ID and update the details
                Model.students.find { it.id == originalId }?.apply {
                    name = updatedName
                    id = updatedId  // ✅ Allow updating the ID
                    phone = updatedPhone
                    address = updatedAddress
                    isChecked = updatedIsChecked
                }

                Log.d("EditStudentActivity", "After Update: ID=${student.id}, Name=${student.name}, Phone=${student.phone}, Address=${student.address}, Checked=${student.isChecked}")
            }



            // ✅ Send updated data back to StudentDetailsActivity
            val resultIntent = Intent().apply {
                putExtra("STUDENT_NAME", updatedName)
                putExtra("STUDENT_ID", updatedId)
                putExtra("STUDENT_ORIGINAL_ID", originalId)
                putExtra("STUDENT_PHONE", updatedPhone)
                putExtra("STUDENT_ADDRESS", updatedAddress)
                putExtra("STUDENT_IS_CHECKED", updatedIsChecked)
            }
            Log.d("EditStudentActivity", "Stduent Original ID: $originalId")
            Log.d("EditStudentActivity", "Updated student details sent back with ID: $updatedId")
            Log.d("EditStudentActivity", "Updated student details sent back with Name: $updatedName")
            Log.d("EditStudentActivity", "Updated student details sent back with Phone: $updatedPhone")
            Log.d("EditStudentActivity", "Updated student details sent back with Address: $updatedAddress")
            Log.d("EditStudentActivity", "Updated student details sent back with IsChecked: $updatedIsChecked")
            setResult(RESULT_OK, resultIntent)
            finish() // Close the activity

        }
        // Delete Button Listener
        deleteButton.setOnClickListener {

            Log.d("EditStudentActivity", "Delete button clicked for ID: $originalId")
            if (originalId.isEmpty()) {
                Log.e("EditStudentActivity", "Error: Cannot delete, ORIGINAL_STUDENT_ID is empty!")
                return@setOnClickListener
            }
            // ✅ Remove the student from the model
            val studentRemoved = Model.students.removeIf { it.id == originalId }
            Log.d("EditStudentActivity", "Student removed: $studentRemoved")
            val resultIntent = Intent().apply {
                putExtra("ORIGINAL_STUDENT_ID", originalId)
                putExtra("IS_DELETED", studentRemoved)  // ✅ Ensure deletion flag is correct
            }
            setResult(RESULT_OK, resultIntent)
            Log.d("EditStudentActivity", "Deletion result sent back for ID: $originalId")
            startActivity(Intent(this, StudentsRecyclerViewActivity::class.java))
            finish()  // ✅ Close activity and return to RecyclerView
        }
    }
}