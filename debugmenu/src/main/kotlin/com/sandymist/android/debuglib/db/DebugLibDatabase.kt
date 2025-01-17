package com.sandymist.android.debuglib.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NetworkLogEntity::class], version = 1, exportSchema = false)
abstract class DebugLibDatabase : RoomDatabase() {
    abstract fun networkLogDao(): NetworkLogDao
}
