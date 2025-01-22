package com.example.matala2_student_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.matala2_student_app.model.Model
import com.example.matala2_student_app.model.Student

class StudentListViewActivity : AppCompatActivity() {

    var students: MutableList<Student>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_list_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // TODO: Step 1 - Set XML Layout
        // TODO: Step 2 - Set Instance Of List View In Activity
        // TODO: Step 3 - Set Adapter
        // TODO: Step 4 - Create Rows Layout
        // TODO: Step 5 - Set Dynamic Data (MVP)
        // TODO: Step 6 - On Click On Checkbox

        students = Model.students
        val listView: ListView = findViewById(R.id.students_list_view) //TODO 2
        listView.adapter = StudentsAdapter()
    }

    inner class StudentsAdapter(): BaseAdapter(){
        override fun getCount(): Int = students?.size ?: 0


        override fun getItem(p0: Int): Any {
            TODO("Not yet implemented")
        }

        override fun getItemId(p0: Int): Long {
            TODO("Not yet implemented")
        }

        //--------- OPTION 1 FOR CHECKBOX (MORE COMMON) -------------
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View{
            val inflation = LayoutInflater.from(parent?.context)
            val view = convertView ?: inflation.inflate(
                R.layout.student_list_row,
                parent,
                false
            ).apply {
                //Log.d("TAG","Inflating position $position")
                findViewById<CheckBox>(R.id.student_row_check_box).apply {
                    setOnClickListener{ view ->
                        (tag as? Int)?.let{ tag ->
                            val student = students?.get(tag)
                            student?.isChecked = (view as? CheckBox)?.isChecked ?: false
                        }
                    }

                }
            }

//---------OPTION 2 FOR CHECKBOX-------------

//            var view =convertView
//            if(view == null){
//                view = inflation.inflate(R.layout.student_list_row,parent,false)
//                Log.d("TAG","Inflating position $position")
//                val checkBox: CheckBox? = view?.findViewById(R.id.student_row_check_box)
//                //checkBox?.setOnClickListener{
//                //     student?.isChecked = checkBox.isChecked
//                // }
//
//                //When we click on the check box
//                checkBox?.apply {
//                    setOnClickListener{ view ->
//                        (tag as? Int)?.let{ tag ->
//                            val student = students?.get(tag)
//                            student?.isChecked = (view as? CheckBox)?.isChecked ?: false
//                        }
//                    }
//                }
//            }


            val student = students?.get(position)
            val nameTextView: TextView? = view?.findViewById(R.id.student_row_name_text_view)
            val idTextView: TextView? = view?.findViewById(R.id.student_row_id_text_view)
            val checkBox: CheckBox? = view?.findViewById(R.id.student_row_check_box)



            nameTextView?.text=student?.name
            idTextView?.text=student?.id
            //checkBox?.isChecked = student?.isChecked ?: false


            checkBox?.apply{
                isChecked = student?.isChecked ?: false
                tag = position
            }


            return view!!
        }

    }
}