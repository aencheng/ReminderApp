package com.example.bonus

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Schema for the Reminder Database

@Entity(tableName = "reminder_table")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    var reminderId: Long = 0L,
    @ColumnInfo(name = "reminder_name")
    var reminderName: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "time")
    var time: String = "",
    @ColumnInfo(name = "date")
    var date: String = "",
    @ColumnInfo(name = "reminder_done")
    var reminderDone: Boolean = false
)