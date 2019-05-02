package ru.zavedyaev.fillme

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicInteger

/**
 * This service will continuously play background music while app is active.
 * To determine that app is active we will listen for all onPause and onResume events from all activities.
 * In case the last action was onPause and there were no other actions in some period of time this service should be self destroyed.
 */
class SoundService : Service() {
    private var backgroundMusic: MediaPlayer? = null
    private var backgroundMusicVolume = 1f
    private var soundVolume = 1f
    /** 0 means active, 1 means on pause */
    private var pauseCounter = AtomicInteger(1)

    override fun onBind(intent: Intent?) = null

    override fun onCreate() {
        super.onCreate()
        val pref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        backgroundMusicVolume = pref.getFloat(SharedPreferencesKey.BACKGROUND_MUSIC_VOLUME.name, 1f)
        soundVolume = pref.getFloat(SharedPreferencesKey.SOUND_VOLUME.name, 1f)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val commandStr = intent?.getStringExtra(COMMAND_EXTRA_NAME)
        val command = commandStr?.let { SoundServiceCommand.valueOf(it) }

        when (command) {
            SoundServiceCommand.PAUSE -> {
                val incremented = pauseCounter.incrementAndGet()
                println("!!! ${this.hashCode()} PAUSE $incremented")
                backgroundMusicControlExecutorService.submit(afterPauseOrResumeSingleThreadRunnable)
            }
            SoundServiceCommand.RESUME -> {
                val decremented = pauseCounter.decrementAndGet()
                println("!!! ${this.hashCode()} RESUME $decremented")
                backgroundMusicControlExecutorService.submit(afterPauseOrResumeSingleThreadRunnable)
            }
            SoundServiceCommand.BACKGROUND_MUSIC_VOLUME_0 -> {
                backgroundMusicVolume = 0f
                backgroundMusic?.setVolume(backgroundMusicVolume, backgroundMusicVolume)
            }
            SoundServiceCommand.BACKGROUND_MUSIC_VOLUME_1 -> {
                backgroundMusicVolume = 1f
                backgroundMusic?.setVolume(backgroundMusicVolume, backgroundMusicVolume)
            }
            SoundServiceCommand.SOUND_VOLUME_0 -> {
                soundVolume = 0f
                //todo setup all
            }
            SoundServiceCommand.SOUND_VOLUME_1 -> {
                soundVolume = 1f
                //todo setup all
            }
            SoundServiceCommand.PLAY_SOUND_X -> TODO()
            else -> TODO()
        }

        return Service.START_STICKY
    }

    private val afterPauseOrResumeSingleThreadRunnable = {
        backgroundMusicStateChangeFutures.forEach { future ->
            future.cancel(true)
        }
        backgroundMusicStateChangeFutures.clear()

        val future = backgroundMusicStateChangeExecutorService.submit(changeStateRunnable)
        backgroundMusicStateChangeFutures.add(future)

    }
    private val backgroundMusicControlExecutorService = Executors.newSingleThreadExecutor()

    private val backgroundMusicStateChangeExecutorService = Executors.newSingleThreadExecutor()
    private val backgroundMusicStateChangeFutures = ArrayList<Future<*>>()

    private val changeStateRunnable = {
        try {
            Thread.sleep(700)
            val paused = pauseCounter.get() == 1
            if (paused) {
                stopSelf()
            } else {
                if (backgroundMusic == null) {
                    val newBackgroundMusic = MediaPlayer.create(this@SoundService, R.raw.music)
                    backgroundMusic = newBackgroundMusic
                    newBackgroundMusic.isLooping = true
                    newBackgroundMusic.setVolume(backgroundMusicVolume, backgroundMusicVolume)
                    newBackgroundMusic.start()
                }
            }
        } catch (e: InterruptedException) {
            //do nothing
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundMusic?.stop()
        backgroundMusic?.release()
        backgroundMusic = null
    }

    companion object {
        const val COMMAND_EXTRA_NAME = "COMMAND_EXTRA"
    }
}

enum class SoundServiceCommand {
    /** Should  */
    PAUSE,
    RESUME,
    BACKGROUND_MUSIC_VOLUME_0,
    BACKGROUND_MUSIC_VOLUME_1,
    SOUND_VOLUME_0,
    SOUND_VOLUME_1,
    PLAY_SOUND_X
}

enum class SharedPreferencesKey {
    BACKGROUND_MUSIC_VOLUME,
    SOUND_VOLUME
}