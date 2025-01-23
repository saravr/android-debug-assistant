package com.sandymist.android.debuglib.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.sandymist.android.debuglib.db.DebugLibDatabase
import com.sandymist.android.debuglib.db.NetworkLogDao
import com.sandymist.android.debuglib.repository.NetworkLogRepository
import com.sandymist.android.debuglib.ui.ui.theme.DebugAssistantTheme
import com.sandymist.android.debuglib.ui.viewmodel.NetworkLogViewModel
import com.sandymist.android.debuglib.ui.viewmodel.NetworkLogViewModelFactory

class DebugActivity : ComponentActivity() {
    private lateinit var database: DebugLibDatabase
    private lateinit var networkLogDao: NetworkLogDao
    private lateinit var networkLogRepository: NetworkLogRepository
    private lateinit var networkLogViewModel: NetworkLogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        database = DebugLibDatabase.getDatabase(this)
        networkLogDao = database.networkLogDao()
        networkLogRepository = NetworkLogRepository(networkLogDao)

        val factory = NetworkLogViewModelFactory(networkLogRepository)
        networkLogViewModel = ViewModelProvider(this, factory)[NetworkLogViewModel::class.java]

        setContent {
            DebugAssistantTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NetworkLogScreen(
                        modifier = Modifier.padding(innerPadding),
                        networkLogViewModel = networkLogViewModel,
                    )
                }
            }
        }
    }
}
