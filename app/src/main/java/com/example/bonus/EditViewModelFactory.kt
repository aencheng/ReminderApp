package com.example.bonus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditViewModelFactory (private val reminderId: Long, private val dao: ReminderDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            return EditViewModel(reminderId, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}