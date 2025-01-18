package com.sandymist.android.debuglib.model

data class NetworkLog(
    val responseCode: Int,
    val url: String,
    val method: String,
    val requestHeaders: String = "",
    val responseHeaders: String = "",
    val body: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val turnaroundTime: Long = 0L,
)
