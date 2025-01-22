package com.example.matala2_student_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matala2_student_app.model.Model
import com.example.matala2_student_app.model.Student
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StudentsRecyclerViewActivity : AppCompatActivity() {
    private lateinit var addStudentLauncher: ActivityResultLauncher<Intent>
    private lateinit var editStudentLauncher: ActivityResultLauncher<Intent>
    private lateinit var studentDetailsLauncher: ActivityResultLauncher<Intent>
    private lateinit var recyclerAdapter: StudentsRecyclerAdapter
    private val students = Model.students


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_students_recycler_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_title_student_recycler_view)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.toolbar_title_student_recycler_view)

        // Set up RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.students_recycler_view)
        recyclerAdapter = StudentsRecyclerAdapter(students) { selectedStudent ->
            // Pass student details to StudentDetailsActivity
            val intent = Intent(this, StudentDetailsActivity::class.java).apply {
                putExtra("STUDENT_ID", selectedStudent.id)
                putExtra("STUDENT_NAME", selectedStudent.name)
                putExtra("STUDENT_AVATAR", selectedStudent.avatarUrl)
                putExtra("STUDENT_PHONE", selectedStudent.phone)
                putExtra("STUDENT_ADDRESS", selectedStudent.address)
                putExtra("STUDENT_IS_CHECKED", selectedStudent.isChecked)
            }
            //editStudentLauncher.launch(intent)
            studentDetailsLauncher.launch(intent)
            //startActivity(intent)
        }
        recyclerView.apply {
            //setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@StudentsRecyclerViewActivity)
            adapter = recyclerAdapter
        }


        //  Add the FAB click listener here
        val fabAddStudent: FloatingActionButton = findViewById(R.id.add_student_button)
        fabAddStudent.setOnClickListener {
            // Open AddStudentActivity when the FAB is clicked
            val intent = Intent(this, AddStudentActivity::class.java)
            addStudentLauncher.launch(intent)
        }
        // Handle Add Student Result
        addStudentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val newStudent = Student(
                        name = result.data?.getStringExtra("STUDENT_NAME") ?: "",
                        id = result.data?.getStringExtra("STUDENT_ID") ?: "",
                        avatarUrl = "",
                        phone = result.data?.getStringExtra("STUDENT_PHONE") ?: "",
                        isChecked = result.data?.getBooleanExtra("STUDENT_IS_CHECKED", false)
                            ?: false,
                        address = result.data?.getStringExtra("STUDENT_ADDRESS") ?: ""
                    )
                    students.add(newStudent)
                    recyclerAdapter.notifyItemInserted(students.size - 1)
                }
            }
        // Handle Edit/Delete Student Result
        editStudentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val originalId = result.data!!.getStringExtra("ORIGINAL_STUDENT_ID")
                    val isDeleted = result.data!!.getBooleanExtra("IS_DELETED", false)
                    Log.d(
                        "StudentsRecyclerViewActivity",
                        "Received deletion result - ID: $originalId, isDeleted: $isDeleted"
                    )
                    if (isDeleted && !originalId.isNullOrEmpty()) {
                        // ✅ Delete the student from the list
                        val deletedIndex = students.indexOfFirst { it.id == originalId }
                        Log.d(
                            "StudentsRecyclerViewActivity",
                            "Deleting student at index: $deletedIndex"
                        )
                        if (deletedIndex != -1) {
                            students.removeAt(deletedIndex)
                            recyclerAdapter.notifyItemRemoved(deletedIndex)  // ✅ Notify RecyclerView
                            Log.d(
                                "StudentsRecyclerViewActivity",
                                "Student with ID $originalId was removed from the list"
                            )
                        }
                    } else {
                        val updatedId = result.data!!.getStringExtra("STUDENT_ID")
                        val updatedName = result.data!!.getStringExtra("STUDENT_NAME")
                        val updatedPhone = result.data!!.getStringExtra("STUDENT_PHONE")
                        val updatedAddress = result.data!!.getStringExtra("STUDENT_ADDRESS")
                        val updatedIsChecked = result.data!!.getBooleanExtra("STUDENT_IS_CHECKED", false)

                        //✅ Find and update the student in the list
                        val updatedIndex = students.indexOfFirst { it.id == updatedId }
                        if (updatedIndex != -1) {
                            students[updatedIndex].apply {
                                id = updatedId ?: id
                                name = updatedName ?: name
                                phone = updatedPhone ?: phone
                                address = updatedAddress ?: address
                                isChecked = updatedIsChecked
                            }
                            Log.d(
                                "StudentsRecyclerViewActivity",
                                "Student with ID $originalId not found in the list"
                            )
                            recyclerAdapter.notifyItemChanged(updatedIndex)  // ✅ Notify adapter of the change

                        } else {
                            Log.d(
                                "StudentsRecyclerViewActivity",
                                "Student with ID $originalId not found in the list"
                            )
                        }
                    }
                }
            }
        studentDetailsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val updatedId = result.data!!.getStringExtra("STUDENT_ID")
                val updatedIndex = students.indexOfFirst { it.id == updatedId }
                if (updatedIndex != -1) {
                    students[updatedIndex].apply {
                        name = result.data!!.getStringExtra("STUDENT_NAME") ?: name
                        phone = result.data!!.getStringExtra("STUDENT_PHONE") ?: phone
                        address = result.data!!.getStringExtra("STUDENT_ADDRESS") ?: address
                        isChecked = result.data!!.getBooleanExtra("STUDENT_IS_CHECKED", isChecked)
                    }
                    recyclerAdapter.notifyItemChanged(updatedIndex)
                }
            }
        }

    }



    class StudentViewHolder(itemView: View, private val onItemClick: (Student) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val nameTextView: TextView = itemView.findViewById(R.id.student_row_name_text_view)
        private val idTextView: TextView = itemView.findViewById(R.id.student_row_id_text_view)
        private val checkBox: CheckBox = itemView.findViewById(R.id.student_row_check_box)


        fun bind(student: Student) {
            nameTextView.text = student.name
            idTextView.text = student.id
            checkBox.isChecked = student.isChecked
            itemView.setOnClickListener { onItemClick(student) }

            //checkBox.apply{
            //    isChecked = student?.isChecked ?: false
            //    tag = position
            //}
        }
    }

    //Everytime that we work with RecyclerView we need to write this code!!!
    class StudentsRecyclerAdapter(private val students: MutableList<Student>, private val onItemClick: (Student) -> Unit):
        RecyclerView.Adapter<StudentViewHolder>() {

        //This function will create new view
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.student_list_row, parent, false)
            return StudentViewHolder(view, onItemClick)
        }

        override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
            holder.bind(students[position])
        }
        override fun getItemCount(): Int = students.size
    }
}