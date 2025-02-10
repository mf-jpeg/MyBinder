package pt.mf.mybinder.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast

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
        val vibrator = MyBinder.ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (!vibrator.hasVibrator()) {
            Logger.error(TAG, "Vibration not available on this device.")
            return
        }

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                vibrator.vibrate(effect)
                Logger.debug(TAG, "Performed Q vibration.")
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                val effect = VibrationEffect.createOneShot(40, 180)
                vibrator.vibrate(effect)
                Logger.debug(TAG, "Performed O vibration.")
            }

            else -> {
                vibrator.vibrate(40)
                Logger.debug(TAG, "Performed legacy vibration.")
            }
        }
    }

    fun String.Companion.empty(): String {
        return ""
    }

    fun toast(message: String) {
        Toast.makeText(MyBinder.ctx, message, Toast.LENGTH_SHORT).show()
    }

    fun intToBool(value: Int): Boolean {
        return value == 1
    }

    fun addEnclosingQuotes(str: String): String {
        return "\"$str\""
    }
}