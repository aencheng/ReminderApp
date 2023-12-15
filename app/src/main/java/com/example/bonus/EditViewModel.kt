package com.example.bonus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class EditViewModel(reminderId: Long, val dao: ReminderDao) : ViewModel() {
    val reminder = dao.get(reminderId)
    var newDate = ""
    var newTime = ""
    private val _navigateToReminder = MutableLiveData<Boolean>(false)
    val navigateToReminder: LiveData<Boolean>
        get() = _navigateToReminder

    // updates task to new value
    fun updateReminder() {
        viewModelScope.launch {
            dao.update(reminder.value!!)
            _navigateToReminder.value = true
        }
    }

    // signals new navigation event
    fun onNavigatedToReminder() {
        _navigateToReminder.value = false
    }

}