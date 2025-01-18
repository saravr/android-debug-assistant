package com.sandymist.android.debuglib.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sandymist.android.common.utilities.ageString
import com.sandymist.android.common.utilities.debouncedClickable
import com.sandymist.android.debuglib.model.NetworkLog
import com.sandymist.android.debuglib.ui.viewmodel.NetworkLogViewModel

@Suppress("unused")
@Composable
fun NetworkLogScreen(
    modifier: Modifier = Modifier,
    networkLogViewModel: NetworkLogViewModel,
) {
    val networkLog by networkLogViewModel.networkLogList.collectAsStateWithLifecycle()

    NetworkLogList(modifier, networkLog) {
        networkLogViewModel.clear()
    }
}

@Composable
fun NetworkLogList(
    modifier: Modifier = Modifier,
    logList: List<NetworkLog>,
    onClear: () -> Unit,
) {
    if (logList.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text("No logs found")
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear all",
                    modifier = Modifier
                        .size(28.dp)
                        .padding(4.dp)
                        .debouncedClickable {
                            onClear()
                        },
                )
            }
            HorizontalDivider(color = Color.LightGray, modifier = Modifier.padding(top = 4.dp))
        }

        items(logList) {
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.175f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = it.responseCode.toString(),
                        color = if (it.responseCode >= 400) Color.Red else LocalContentColor.current,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(it.method)
                }
                Text(text = it.url, maxLines = 3, overflow = TextOverflow.Ellipsis)
            }

            val age = it.timestamp / 1000
            Text(
                text = age.ageString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
            )

            HorizontalDivider(color = Color.LightGray, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Preview
@Composable
fun PreviewNetworkLogScreen() {
    val logList = listOf(
        NetworkLog(
            responseCode = 201,
            url = "https://url.com",
            method = "GET",
        ),
        NetworkLog(
            responseCode = 403,
            url = "https://example.com",
            method = "POST",
        ),
    )
    NetworkLogList(logList = logList) {}
}

@Preview
@Composable
fun PreviewEmptyNetworkLogScreen() {
    NetworkLogList(logList = emptyList()) {}
}
