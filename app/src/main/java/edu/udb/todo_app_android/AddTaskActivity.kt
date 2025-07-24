package edu.udb.todo_app_android

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TASK = "edu.udb.todo_app_android.EXTRA_TASK"
    }

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etDate: EditText
    private lateinit var etCategory: EditText
    private lateinit var cbUrgent: CheckBox
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        etTitle = findViewById(R.id.et_title)
        etDescription = findViewById(R.id.et_description)
        etDate = findViewById(R.id.et_date)
        etCategory = findViewById(R.id.et_category)
        cbUrgent = findViewById(R.id.cb_urgent)

        val btnCreate = findViewById<Button>(R.id.btn_create)
        val btnBack = findViewById<ImageView>(R.id.btn_back)

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        etDate.setOnClickListener {
            DatePickerDialog(
                this@AddTaskActivity,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnCreate.setOnClickListener {
            saveTask()
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        etDate.setText(sdf.format(calendar.time))
    }

    private fun saveTask() {
        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim().takeIf { it.isNotEmpty() }
        val date = etDate.text.toString().trim()
        val category = etCategory.text.toString().trim()
        val isUrgent = cbUrgent.isChecked

        // Validaciones básicas
        if (title.isEmpty()) {
            etTitle.error = "El título es obligatorio"
            etTitle.requestFocus()
            return
        }
        if (date.isEmpty()) {
            etDate.error = "La fecha es obligatoria"
            Toast.makeText(this, "Por favor, selecciona una fecha", Toast.LENGTH_SHORT).show()
            return
        }
        if (category.isEmpty()) {
            etCategory.error = "La categoría es obligatoria"
            etCategory.requestFocus()
            return
        }


        val newTask = Task(
            title = title,
            description = description,
            date = date,
            category = category,
            isUrgent = isUrgent
        )

        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_TASK, newTask)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}