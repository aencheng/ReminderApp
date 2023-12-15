package com.example.bonus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bonus.databinding.ReminderItemBinding

class ReminderItemAdapter(val clickListener: (reminderId: Long) -> Unit,
                          val deleteClickListener: (reminderId: Long) -> Unit
) : ListAdapter<Reminder, ReminderItemAdapter.TaskItemViewHolder>(ReminderDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : TaskItemViewHolder = TaskItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, deleteClickListener)
    }

    class TaskItemViewHolder(val binding: ReminderItemBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): TaskItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReminderItemBinding.inflate(layoutInflater, parent, false)
                return TaskItemViewHolder(binding)
            }
        }

        fun bind(
            item: Reminder, clickListener: (reminderId: Long) -> Unit,
            deleteClickListener: (reminderId: Long) -> Unit
        ) {
            binding.reminder = item
            binding.root.setOnClickListener { clickListener(item.reminderId) }
            binding.deleteButton.setOnClickListener { deleteClickListener(item.reminderId) }
        }
    }
}