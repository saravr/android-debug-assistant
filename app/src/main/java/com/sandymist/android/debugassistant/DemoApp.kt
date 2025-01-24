package com.sandymist.android.debugassistant

import android.app.Application
import com.sandymist.android.debuglib.DebugLib
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DemoApp: Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        DebugLib.init(this)
    }
}
