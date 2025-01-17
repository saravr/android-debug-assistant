package com.sandymist.android.debugassistant.demo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sandymist.android.debugassistant.ui.theme.DebugAssistantTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val client = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DebugAssistantTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding),
                        performGetRequest = {
                            performGetRequest(it)
                        }
                    )
                }
            }
        }
    }

    private fun performGetRequest(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body?.string() ?: throw IOException("Empty response body")
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier, performGetRequest: (String) -> String,
) {
    val scope = rememberCoroutineScope()
    var response by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            val url = "https://jsonplaceholder.typicode.com/posts/1"
            response = try {
                response = performGetRequest(url)
                Log.d("OkHttpExample", response)
                response
            } catch (e: Exception) {
                Log.e("OkHttpExample", "Error: ${e.message}")
                "Exception: ${e.javaClass.name} - ${e.message}"
            }
        }
    }

    Text(
        text = "Response: $response",
        modifier = modifier
    )
}

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("LoggingInterceptor", "Request URL: ${request.url}")
        Log.d("LoggingInterceptor", "Request Headers: ${request.headers}")

        val response = chain.proceed(request)

        Log.d("LoggingInterceptor", "Response Code: ${response.code}")
        Log.d("LoggingInterceptor", "Response Headers: ${response.headers}")

        return response
    }
}
