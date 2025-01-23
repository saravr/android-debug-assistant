@file:Suppress("PackageDirectoryMismatch")
package com.sandymist.mobile.plugins.network

import com.sandymist.android.debuglib.DebugLib
import com.sandymist.android.debuglib.model.NetworkLog
import okhttp3.Interceptor
import okhttp3.Response

@Suppress("unused")
class NetworkPlugin: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request.url()
        val startTime = System.nanoTime()
        val response: Response = chain.proceed(request)
        val endTime = System.nanoTime()

        val networkLog = NetworkLog(
            responseCode = response.code(),
            url = url.toString(),
            method = request.method(),
            body = "", //response.body().toString(),
            requestHeaders = "", //request.headers().toString(),
            responseHeaders = "", //response.headers().toString(),
            timestamp = System.currentTimeMillis(),
            turnaroundTime = endTime - startTime,
        )
        DebugLib.insertNetworkLog(networkLog)
        return response
    }
}
