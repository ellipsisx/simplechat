package dk.ellipsisx.simplechat

import android.app.Application
import android.content.Context
import dk.ellipsisx.simplechat.util.Log

class App : Application() {

    companion object {
        lateinit var appContext: Context
        lateinit var appCore: AppCore
    }

    override fun onCreate() {
        super.onCreate()

        initialize()
    }

    private fun initialize() {
        Log.d("App starting - setting Log silent if release")
        Log.setSilent(!BuildConfig.DEBUG)
        Log.d("Log is not silent")

        appContext = this
        appCore = AppCore()
    }

}
