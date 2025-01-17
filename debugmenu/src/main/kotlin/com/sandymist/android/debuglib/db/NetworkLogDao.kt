package com.sandymist.android.debuglib.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkLogDao {
    @Insert
    suspend fun insert(entity: NetworkLogEntity)

    @Query("SELECT * FROM network_log ORDER BY timestamp DESC")
    fun getAll(): Flow<List<NetworkLogEntity>>
}
