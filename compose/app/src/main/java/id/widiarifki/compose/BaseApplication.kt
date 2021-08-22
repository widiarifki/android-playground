package id.widiarifki.compose

import android.app.Application
import id.widiarifki.compose.data.AppDatabase

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.initialize(applicationContext)
    }
}