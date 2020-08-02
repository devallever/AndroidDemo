package com.allever.app.demo

import android.app.Application
import leakcanary.LeakCanary

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}