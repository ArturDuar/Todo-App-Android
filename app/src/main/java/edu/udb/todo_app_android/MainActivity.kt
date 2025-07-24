package edu.udb.todo_app_android

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope // Importante para coroutines
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch // Importante para coroutines

class MainActivity : AppCompatActivity() {

    private lateinit var rvTasks: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    private lateinit var db: AppDatabase
    private lateinit var taskDao: TaskDao
    // ---------------------------

    private val addTaskResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val newTask: Task? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    data.getParcelableExtra(AddTaskActivity.EXTRA_TASK, Task::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    data.getParcelableExtra<Task>(AddTaskActivity.EXTRA_TASK)
                }

                newTask?.let {
                    lifecycleScope.launch {
                        taskDao.insertTask(it)
                    }
                }
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeIfNeeded()
        setContentView(R.layout.activity_main)

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        rvTasks = findViewById(R.id.rvTasks)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        // --- Inicializar Room ---
        db = AppDatabase.getDatabase(applicationContext)
        taskDao = db.taskDao()

        setupRecyclerView() // Configura el adaptador
        observeTasks()    // Comienza a observar los datos de la base de datos

        fabAdd.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            addTaskResultLauncher.launch(intent)
        }
    }

    private fun enableEdgeToEdgeIfNeeded() {
        // androidx.activity.enableEdgeToEdge() // Descomenta si es necesario
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onTaskCompletedChanged = { task, isCompleted ->
                val updatedTask = task.copy(isCompleted = isCompleted)
                lifecycleScope.launch {
                    taskDao.updateTask(updatedTask)
                }
            },
            onDeleteClicked = { task ->
                lifecycleScope.launch {
                    taskDao.deleteTask(task)
                    // Opcional: Mostrar un Toast de confirmación
                    Toast.makeText(this@MainActivity, "Tarea eliminada: ${task.title}", Toast.LENGTH_SHORT).show()
                }
            }
        )
        rvTasks.adapter = taskAdapter
        rvTasks.layoutManager = LinearLayoutManager(this)
    }

    private fun observeTasks() {
        taskDao.getAllTasks().observe(this) { tasksFromDb ->
            tasksFromDb?.let {
                taskAdapter.submitList(it)
            }
        }
    }
}