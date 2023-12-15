package com.example.bonus

import androidx.recyclerview.widget.DiffUtil

class ReminderDiffItemCallback : DiffUtil.ItemCallback<Reminder>(){
    override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder) = (oldItem.reminderId == newItem.reminderId)
    override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder) = (oldItem == newItem)
}