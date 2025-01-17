package com.sandymist.android.debuglib.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "network_log")
data class NetworkLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)
