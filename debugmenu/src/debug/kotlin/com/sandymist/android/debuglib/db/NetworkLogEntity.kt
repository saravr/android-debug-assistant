package com.sandymist.android.debuglib.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sandymist.android.debuglib.model.NetworkLog

@Entity(tableName = "network_log")
data class NetworkLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val responseCode: Int,
    val url: String,
    val method: String,
    val requestHeaders: String,
    val responseHeaders: String,
    val body: String,
    val timestamp: Long,
    val turnaroundTime: Long,
) {
    fun toNetworkLog(): NetworkLog {
        return NetworkLog(
            responseCode = responseCode,
            url = url,
            method = method,
            requestHeaders = requestHeaders,
            responseHeaders = responseHeaders,
            body = body,
            timestamp = timestamp,
            turnaroundTime = turnaroundTime,
        )
    }

    companion object {
        fun fromNetworkLog(networkLog: NetworkLog): NetworkLogEntity {
            return NetworkLogEntity(
                responseCode = networkLog.responseCode,
                url = networkLog.url,
                method = networkLog.method,
                requestHeaders = networkLog.requestHeaders.toString(),
                responseHeaders = networkLog.responseHeaders.toString(),
                body = networkLog.body,
                timestamp = networkLog.timestamp,
                turnaroundTime = networkLog.turnaroundTime,
            )
        }
    }
}
