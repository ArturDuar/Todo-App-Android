package edu.udb.todo_app_android

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val onTaskCompletedChanged: (task: Task, isCompleted: Boolean) -> Unit,
    private val onDeleteClicked: (task: Task) -> Unit,
    private val onItemClick: (task: Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.bind(currentTask)
        holder.itemView.setOnClickListener { onItemClick(currentTask) }
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPriority: TextView = itemView.findViewById(R.id.tvPriority)
        private val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val cbCompleted: CheckBox = itemView.findViewById(R.id.cbCompleted)

        fun bind(task: Task) {
            tvTitle.text = task.title
            tvDescription.text = task.description ?: ""
            tvDescription.visibility = if (task.description.isNullOrEmpty()) View.GONE else View.VISIBLE
            tvCategory.text = task.category
            tvDate.text = task.date

            if (task.isUrgent) {
                tvPriority.text = "URGENTE"
                tvPriority.setBackgroundColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))
                tvPriority.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                tvPriority.visibility = View.VISIBLE
            } else {
                tvPriority.visibility = View.GONE
            }

            cbCompleted.isChecked = task.isCompleted
            updateTitleStrikeThrough(task.isCompleted)
            cbCompleted.text = if (task.isCompleted) "Completado" else "No completado"

            cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                if (task.isCompleted != isChecked) {
                    onTaskCompletedChanged(task, isChecked)
                }
            }

            btnDelete.setOnClickListener {
                onDeleteClicked(task)
            }
        }

        private fun updateTitleStrikeThrough(isCompleted: Boolean) {
            if (isCompleted) {
                tvTitle.paintFlags = tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvDescription.paintFlags = tvDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvTitle.paintFlags = tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                tvDescription.paintFlags = tvDescription.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}