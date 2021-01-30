package allever.android.lib.compent.common.util

import android.app.Application
import android.content.Context

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context: Context
    }
}