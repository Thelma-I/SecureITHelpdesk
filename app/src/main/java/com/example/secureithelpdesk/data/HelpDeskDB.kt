package com.example.secureithelpdesk.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [HelpdeskTicket::class],
    version = 1,
    exportSchema = false
)
abstract class HelpdeskDatabase : RoomDatabase() {

    abstract fun ticketDao(): TicketDao

    companion object {
        @Volatile
        private var INSTANCE: HelpdeskDatabase? = null

        fun getDatabase(context: Context): HelpdeskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HelpdeskDatabase::class.java,
                    "helpdesk_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
