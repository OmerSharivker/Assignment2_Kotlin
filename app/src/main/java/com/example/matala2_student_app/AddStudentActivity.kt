package com.example.matala2_student_app

import android.content.Intent
import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import com.example.matala2_student_app.model.Model
import com.example.matala2_student_app.model.Student

class AddStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_title_add_student)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.toolbar_title_add_student)


        //-----DEFINE----
        val saveButton: Button = findViewById(R.id.add_student_activity_save_button)
        val cancelButton: Button = findViewById(R.id.add_student_activity_cancel_button)
        val nameEditText: EditText = findViewById(R.id.add_student_activity_name_text)
        val idEditText: EditText = findViewById(R.id.add_student_activity_id_edit_text)
        val savedMessageTextView: TextView = findViewById(R.id.add_student_activity_saved_message_text_view)
        val phoneEditText: EditText = findViewById(R.id.add_student_activity_phone_edit_text)
        val addressEditText: EditText = findViewById(R.id.add_student_activity_address_address_text)
        val checkBox: CheckBox = findViewById(R.id.add_student_activity_checkbox)
        val avatarImageView: ImageView = findViewById(R.id.add_student_image_view2)



        //-----LISTENERS------
        cancelButton.setOnClickListener{
            finish()
        }

        saveButton.setOnClickListener {
            val newStudent = Student(
                name = nameEditText.text.toString(),
                id = idEditText.text.toString(),
                avatarUrl = "",
                phone = phoneEditText.text.toString(),
                isChecked = checkBox.isChecked,
                address = addressEditText.text.toString()
            )

            val resultIntent = Intent().apply {
                putExtra("STUDENT_NAME", newStudent.name)
                putExtra("STUDENT_ID", newStudent.id)
                putExtra("STUDENT_PHONE", newStudent.phone)
                putExtra("STUDENT_ADDRESS", newStudent.address)
                putExtra("STUDENT_IS_CHECKED", newStudent.isChecked)
            }
            setResult(RESULT_OK, resultIntent)
            finish() // Close the activity

        }
    }
}