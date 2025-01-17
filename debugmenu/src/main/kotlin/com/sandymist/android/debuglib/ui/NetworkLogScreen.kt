package com.sandymist.android.debuglib.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sandymist.android.common.utilities.ageString
import com.sandymist.android.debuglib.ui.viewmodel.NetworkLogViewModel

@Composable
fun NetworkLogScreen(networkLogViewModel: NetworkLogViewModel) {
    val networkLog by networkLogViewModel.networkLogList.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(networkLog) {
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.175f)
                ) {
                    Text(it.responseCode.toString())
                    Text(it.method)
                }
                Text(it.url)
            }

            val age = it.timestamp / 1000
            Text(
                text = age.ageString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
            )
            HorizontalDivider(color = Color.LightGray)
        }
    }
}
