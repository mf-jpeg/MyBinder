package pt.mf.mybinder.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * Created by Martim Ferreira on 07/02/2025
 */
object Utils {
    private const val TAG = "Utils"

    fun getString(id: Int): String {
        return MyBinder.ctx.getString(id)
    }

    fun isSystemInDarkMode(): Boolean {
        val flags = MyBinder.ctx.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flags == Configuration.UI_MODE_NIGHT_YES
    }

    fun tactileFeedback() {
        Logger.debug(TAG, "Attempting to perform tactile feedback.")
        val vibrator = MyBinder.ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (!vibrator.hasVibrator()) {
            Logger.error(TAG, "Vibration not available on this device.")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(50, 255)
            vibrator.vibrate(effect)

            Logger.debug(TAG, "Performed modern vibration.")
            return
        }

        vibrator.vibrate(50)
        Logger.debug(TAG, "Performed legacy vibration.")
    }
}