package com.example.bonus

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Functions for Database

@Dao
interface ReminderDao {
    @Insert
    suspend fun insert(reminder: Reminder)

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

    @Query("SELECT * FROM reminder_table WHERE reminderId = :key")
    fun get(key: Long): LiveData<Reminder>

    @Query("SELECT * FROM reminder_table ORDER BY reminderId DESC")
    fun getAll(): LiveData<List<Reminder>>
}