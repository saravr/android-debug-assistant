package com.sandymist.android.debuglib.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sandymist.android.debuglib.ui.viewmodel.NetworkLogViewModel

@Composable
fun NetworkLogScreen(networkLogViewModel: NetworkLogViewModel) {
    val networkLog by networkLogViewModel.networkLogList.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(networkLog) {
            Text(text = it, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
        }
    }
}
