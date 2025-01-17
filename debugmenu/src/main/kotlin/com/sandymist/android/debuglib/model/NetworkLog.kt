package com.sandymist.android.debuglib.model

data class NetworkLog(
    val responseCode: Int = 0,
    val url: String,
    val method: String = "",
    val requestHeaders: String = "",
    val responseHeaders: String = "",
    val body: String = "",
    val timestamp: Long = 0L,
    val turnaroundTime: Long = 0L,
)
