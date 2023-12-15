package com.example.bonus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

suspend fun <T> LiveData<T>.await(): T{
    return withContext(Dispatchers.Main.immediate){
        suspendCancellableCoroutine { continuation ->
            val observer = object : Observer<T> {
                override fun onChanged(value: T){
                    removeObserver(this)
                    continuation.resume(value)
                }
            }
            observeForever(observer)
            continuation.invokeOnCancellation {
                removeObserver(observer)
            }
        }
    }
}

class ReminderViewModel(val dao: ReminderDao) : ViewModel() {
    var newReminderName = ""
    var newDesc = ""
    var newTime = ""
    var newDate = ""
    val reminders = dao.getAll()
    private val _navigateToReminder = MutableLiveData<Long?>()
    val navigateToReminder: LiveData<Long?>
        get() = _navigateToReminder

    fun addReminder(){
        viewModelScope.launch {
            val reminder = Reminder()
            reminder.reminderName = newReminderName
            reminder.description = newDesc
            reminder.time = newTime
            reminder.date = newDate
            dao.insert(reminder)
        }
    }

    fun onReminderClicked(reminderId: Long) {
        _navigateToReminder.value = reminderId
    }

    fun onReminderNavigated() {
        _navigateToReminder.value = null
    }

    fun deleteNote(reminderId: Long) {
        viewModelScope.launch {
            // block until we get the note
            val reminder = dao.get(reminderId).await()
            dao.delete(reminder)
            _navigateToReminder.value = null
        }
    }
}