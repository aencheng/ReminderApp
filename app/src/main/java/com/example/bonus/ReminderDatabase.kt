package com.example.bonus

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Reminder::class], version = 3, exportSchema = false)
abstract class ReminderDatabase : RoomDatabase() {
    abstract val reminderDao: ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: ReminderDatabase? = null
        // database singleton
        fun getInstance(context: Context): ReminderDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ReminderDatabase::class.java,
                        "reminders_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}