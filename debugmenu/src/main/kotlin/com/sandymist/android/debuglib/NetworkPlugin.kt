@file:Suppress("PackageDirectoryMismatch")
package com.sandymist.mobile.plugins.network

import com.sandymist.android.debuglib.DebugLib
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

@Suppress("unused")
class NetworkPlugin: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Timber.e("++++ LOG INTERCEPTOR INVOKED")
        val request = chain.request()

        // Log the request details
        val url = request.url()
        DebugLib.insertLog(url.toString())
        Timber.e("Sending request to URL: $url")
        Timber.e("Request method: ${request.method()}")
        Timber.e("Request headers: ${request.headers()}")

        val startTime = System.nanoTime()
        val response: Response = chain.proceed(request)
        val endTime = System.nanoTime()

        // Log the response details
        Timber.e("Received response for URL: ${response.request().url()}")
        Timber.e("Response code: ${response.code()}")
        Timber.e("Response headers: ${response.headers()}")
        Timber.e("Request-Response time: ${(endTime - startTime) / 1e6} ms")

        return response
    }
}
