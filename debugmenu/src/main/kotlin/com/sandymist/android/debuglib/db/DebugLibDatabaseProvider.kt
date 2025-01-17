package com.sandymist.android.debuglib.db

import android.content.Context
import androidx.room.Room

object DebugLibDatabaseProvider {
    @Volatile
    private var INSTANCE: DebugLibDatabase? = null

    fun getDatabase(context: Context): DebugLibDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                DebugLibDatabase::class.java,
                "debug_lib_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
