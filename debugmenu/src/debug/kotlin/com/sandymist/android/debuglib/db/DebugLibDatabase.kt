package com.sandymist.android.debuglib.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DB_VERSION = 2
private const val DB_NAME = "debug_lib_database"

@Database(entities = [NetworkLogEntity::class], version = DB_VERSION, exportSchema = true)
abstract class DebugLibDatabase : RoomDatabase() {
    abstract fun networkLogDao(): NetworkLogDao

    companion object {
        @Volatile
        private var INSTANCE: DebugLibDatabase? = null

        fun getDatabase(context: Context): DebugLibDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DebugLibDatabase::class.java,
                    DB_NAME,
                )
                    .fallbackToDestructiveMigration() // TODO: remove before launch
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
