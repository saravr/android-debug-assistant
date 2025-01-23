package com.sandymist.android.debugassistant

import android.app.Application
import com.sandymist.android.debuglib.DebugLib
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DemoApp: Application() {
    override fun onCreate() {
        super.onCreate()

        DebugLib.init(this)
    }
}
