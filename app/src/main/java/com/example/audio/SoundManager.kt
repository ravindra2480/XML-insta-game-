package com.example.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoundManager(private val context: Context) {
    private var toneGenerator: ToneGenerator? = null
    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        } catch (e: Exception) {
            // In case tone generator fails to initialize
            toneGenerator = null
        }
    }

    fun playClick(soundEnabled: Boolean = true) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 35)
        } catch (e: Exception) {
            // Ignored
        }
    }

    fun playDialTurn(soundEnabled: Boolean = true) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 45)
        } catch (e: Exception) {
            // Ignored
        }
    }

    fun playSuccessStep(soundEnabled: Boolean = true) {
        if (!soundEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 80)
            } catch (e: Exception) {
                // Ignored
            }
        }
    }

    fun playVictoryFanfare(soundEnabled: Boolean = true) {
        if (!soundEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 100)
                delay(120)
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_MED_L, 100)
                delay(120)
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 250)
            } catch (e: Exception) {
                // Ignored
            }
        }
    }

    fun playTrapBuzz(soundEnabled: Boolean = true) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 150)
        } catch (e: Exception) {
            // Ignored
        }
    }

    fun playTrapError(soundEnabled: Boolean = true) {
        playTrapBuzz(soundEnabled)
    }

    fun playHintChime(soundEnabled: Boolean = true) {
        if (!soundEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 100)
                delay(100)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 120)
            } catch (e: Exception) {
                // Ignored
            }
        }
    }

    fun vibrateShort(hapticsEnabled: Boolean = true) {
        if (!hapticsEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(30)
            }
        } catch (e: Exception) {
            // Ignored
        }
    }

    fun vibrateSuccess(hapticsEnabled: Boolean = true) {
        if (!hapticsEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 50, 70, 100), -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 50, 70, 100), -1)
            }
        } catch (e: Exception) {
            // Ignored
        }
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}
