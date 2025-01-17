package com.sandymist.android.debuglib

import android.content.Context
import com.sandymist.android.debuglib.db.DebugLibDatabase
import com.sandymist.android.debuglib.db.DebugLibDatabaseProvider
import com.sandymist.android.debuglib.db.NetworkLogDao
import com.sandymist.android.debuglib.db.NetworkLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object DebugLib {
    private val networkLogFlow = MutableStateFlow("")
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var database: DebugLibDatabase
    private lateinit var networkLogDao: NetworkLogDao

    @Suppress("unused")
    fun init(context: Context) {
        database = DebugLibDatabaseProvider.getDatabase(context)
        networkLogDao = database.networkLogDao()

        scope.launch {
            networkLogFlow.collect {
                networkLogDao.insert(NetworkLogEntity(name = it))
            }
        }
    }

    fun insertLog(log: String) {
        scope.launch {
            networkLogFlow.emit(log)
        }
    }
}
