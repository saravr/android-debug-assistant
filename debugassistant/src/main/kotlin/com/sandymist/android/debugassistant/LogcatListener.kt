package com.sandymist.android.debugassistant

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class LogcatListener(
    packageName: String,
    minLogLevel: Int = android.util.Log.DEBUG,
    private val log: (String) -> Unit,
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    init {
        scope.launch {
            startListening()
        }
    }

    private val logcatCommand = arrayOf(
        "logcat",
        "-v", "time", // Use time format
        "*:${minLogLevel}", // Set minimum log level
        "*:S", // Suppress other log levels
        "$packageName:*" // Listen for the specific package
    )

    // Function to start the Logcat listener and emit logs to a Flow
    private fun listenForLogs(): Flow<String> = flow {
        val process = ProcessBuilder(*logcatCommand).start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))

        // Continuously read logcat output and emit it to the flow
        reader.use {
            while (true) {
                val logLine = it.readLine()
                if (logLine != null) {
                    emit(logLine)
                } else {
                    delay(100) // Slight delay to avoid maxing out CPU if no logs are present
                }
            }
        }
    }
        .flowOn(Dispatchers.IO)

    // Start listening in a coroutine
    private suspend fun startListening() {
        listenForLogs().collect { logMessage ->
            // Process the log message here (e.g., print to console or save)
            println(logMessage)
            log(logMessage)
        }
    }
}
