package pt.mf.mybinder

import android.app.Application

/**
 * Created by Martim Ferreira on 07/02/2025
 */
class MyBinder : Application() {
    private object HOLDER {
        const val TAG = "MyBinder"
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context: MyBinder
            private set
    }
}