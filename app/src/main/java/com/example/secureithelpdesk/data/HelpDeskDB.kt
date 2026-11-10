
package com.example.secureithelpdesk.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [HelpdeskTicket::class],
    version = 2,
    exportSchema = false
)
abstract class HelpdeskDatabase : RoomDatabase() {
    abstract fun ticketDao(): TicketDao
    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE helpdesk_tickets ADD COLUMN assignedTechnicianId TEXT"
                )
                db.execSQL(
                    "ALTER TABLE helpdesk_tickets ADD COLUMN assignedTechnicianName TEXT"
                )
                db.execSQL(
                    "ALTER TABLE helpdesk_tickets ADD COLUMN acceptedAt INTEGER"
                )
                db.execSQL(
                    "ALTER TABLE helpdesk_tickets ADD COLUMN completedAt INTEGER"
                )
            }
        }

        @Volatile
        private var INSTANCE: HelpdeskDatabase? = null

        fun getDatabase(context: Context): HelpdeskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HelpdeskDatabase::class.java,
                    "helpdesk_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
