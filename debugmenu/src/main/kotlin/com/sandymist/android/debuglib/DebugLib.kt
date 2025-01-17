package com.sandymist.android.debuglib

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.sandymist.android.debuglib.db.DebugLibDatabase
import com.sandymist.android.debuglib.db.DebugLibDatabaseProvider
import com.sandymist.android.debuglib.db.NetworkLogDao
import com.sandymist.android.debuglib.db.NetworkLogEntity
import com.sandymist.android.debuglib.repository.NetworkLogRepository
import com.sandymist.android.debuglib.ui.viewmodel.NetworkLogViewModel
import com.sandymist.android.debuglib.ui.viewmodel.NetworkLogViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object DebugLib {
    private val networkLogFlow = MutableStateFlow("")
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var database: DebugLibDatabase
    private lateinit var networkLogDao: NetworkLogDao
    private lateinit var networkLogRepository: NetworkLogRepository
    lateinit var networkLogViewModel: NetworkLogViewModel
    private val viewModelStore = ViewModelStore()

    @Suppress("unused")
    fun init(context: Context) {
        database = DebugLibDatabaseProvider.getDatabase(context)
        networkLogDao = database.networkLogDao()
        networkLogRepository = NetworkLogRepository(networkLogDao)

        val factory = NetworkLogViewModelFactory(networkLogRepository)
        networkLogViewModel = ViewModelProvider(viewModelStore, factory)[NetworkLogViewModel::class.java]

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

    fun clear() {
        viewModelStore.clear()
        scope.cancel()
    }
}
