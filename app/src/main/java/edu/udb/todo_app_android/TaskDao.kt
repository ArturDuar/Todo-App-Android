package edu.udb.todo_app_android

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks_table ORDER BY id DESC")
    fun getAllTasks(): LiveData<List<Task>> // LiveData para observar cambios automáticamente

    @Query("SELECT * FROM tasks_table WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long): Task?

    // Puedes añadir más consultas según necesites, por ejemplo:
    // @Query("SELECT * FROM tasks_table WHERE isCompleted = :completed ORDER BY date ASC")
    // fun getTasksByCompletionStatus(completed: Boolean): LiveData<List<Task>>
}