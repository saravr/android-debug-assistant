package com.sandymist.android.debuglib.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NetworkLogDao {
    @Insert
    suspend fun insert(entity: NetworkLogEntity)

    @Query("SELECT * FROM network_log")
    suspend fun getAll(): List<NetworkLogEntity>
}
