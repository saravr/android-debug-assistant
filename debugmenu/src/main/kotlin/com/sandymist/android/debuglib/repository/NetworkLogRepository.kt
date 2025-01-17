package com.sandymist.android.debuglib.repository

import com.sandymist.android.debuglib.db.NetworkLogDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NetworkLogRepository(
    private val networkLogDao: NetworkLogDao,
) {
    private val _networkLogList = MutableStateFlow<List<String>>(emptyList())
    val networkLogList = _networkLogList.asStateFlow()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            networkLogDao.getAll().collectLatest {
                _networkLogList.emit(it.map { entity -> entity.name })
            }
        }
    }
}
