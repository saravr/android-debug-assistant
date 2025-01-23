package com.sandymist.android.debugassistant.demo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sandymist.android.debugassistant.ui.theme.DebugAssistantTheme
import com.sandymist.android.debuglib.ui.DebugActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.IOException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Logs request and response bodies
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DebugAssistantTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DebugScreen(
                        modifier = Modifier.padding(innerPadding),
                        performGetRequest = {
                            performGetRequest(it)
                        },
                    ) {
                        val intent = Intent(this, DebugActivity::class.java)
                        startActivity(intent)
                    }
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
fun DebugScreen(
    modifier: Modifier = Modifier,
    performGetRequest: (String) -> String,
    launchDebugActivity: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var counter by remember { mutableIntStateOf(5) }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            while (--counter >= 0) {
                val url = "https://jsonplaceholder.typicode.com/posts/1"
                try {
                    val response = performGetRequest(url)
                    Timber.d(response)
                } catch (e: Exception) {
                    Timber.e("Error: ${e.message}")
                    "Exception: ${e.javaClass.name} - ${e.message}"
                }
                delay(2000L)
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                launchDebugActivity()
            }
        ) {
            Text("Launch Debug Screen")
        }
    }
}
