package pt.mf.mybinder.utils

import android.app.Application
import pt.mf.mybinder.utils.MyBinder.HOLDER.TAG

/**
 * Created by Martim Ferreira on 07/02/2025
 */
class MyBinder : Application() {
    private object HOLDER {
        const val TAG = "MyBinder"
    }

    override fun onCreate() {
        super.onCreate()
        ctx = this
        Logger.debug(TAG, "MyBinder initialized.")
    }

    companion object {
        lateinit var ctx: MyBinder
            private set
    }
}