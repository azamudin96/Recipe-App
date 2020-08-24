package Util

import android.app.Application
import android.content.Context

//App.kt
class AppContext : Application() {
    companion object {
        lateinit var instance: AppContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}