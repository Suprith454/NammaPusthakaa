package com.nammapusthakaa

import android.app.Application
import com.nammapusthakaa.di.AppContainer

class NammaPusthakaaApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
