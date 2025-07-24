package edu.udb.todo_app_android


import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import androidx.room.PrimaryKey

@Parcelize
@Entity(tableName = "tasks_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    var title: String,
    var description: String?,
    var date: String,
    var category: String,
    var isUrgent: Boolean = false,
    var isCompleted: Boolean = false
) : Parcelable