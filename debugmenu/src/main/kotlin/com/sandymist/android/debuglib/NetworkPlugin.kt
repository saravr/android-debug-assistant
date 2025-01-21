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

//        Timber.e("Request method: ${request.method()}")
//        Timber.e("Request headers: ${request.headers()}")

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

//        // Log the response details
//        Timber.e("Received response for URL: ${response.request().url()}")
//        Timber.e("Response code: ${}")
//        Timber.e("Response headers: ${response.headers()}")
//        Timber.e("Request-Response time: ${(endTime - startTime) / 1e6} ms")

        return response
    }
}
