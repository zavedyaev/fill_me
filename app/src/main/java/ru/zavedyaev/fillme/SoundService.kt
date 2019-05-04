package ru.zavedyaev.fillme

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
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
    private lateinit var buttonSound: MediaPlayer
    private lateinit var circleDrawSound: MediaPlayer
    private lateinit var circleFailedSound: MediaPlayer
    private lateinit var circleSuccessSound: MediaPlayer
    private lateinit var looseSound: MediaPlayer
    private lateinit var winSound: MediaPlayer

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

        buttonSound = MediaPlayer.create(this@SoundService, R.raw.button)
        circleDrawSound = MediaPlayer.create(this@SoundService, R.raw.circle_draw)
        circleFailedSound = MediaPlayer.create(this@SoundService, R.raw.failed_circle)
        circleSuccessSound = MediaPlayer.create(this@SoundService, R.raw.success_circle)
        looseSound = MediaPlayer.create(this@SoundService, R.raw.loose)
        winSound = MediaPlayer.create(this@SoundService, R.raw.win)

        updateSoundVolume()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val commandStr = intent?.getStringExtra(COMMAND_EXTRA_NAME)
        val command = commandStr?.let { SoundServiceCommand.valueOf(it) }

        when (command) {
            SoundServiceCommand.PAUSE -> {
                pauseCounter.incrementAndGet()
                backgroundMusicControlExecutorService.submit(afterPauseOrResumeSingleThreadRunnable)
            }
            SoundServiceCommand.RESUME -> {
                pauseCounter.decrementAndGet()
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
                updateSoundVolume()
            }
            SoundServiceCommand.SOUND_VOLUME_1 -> {
                soundVolume = 1f
                updateSoundVolume()
            }
            SoundServiceCommand.PLAY_SOUND_BUTTON -> buttonSound.start()

            SoundServiceCommand.PLAY_SOUND_CIRCLE_DRAW -> circleDrawSound.start()
            SoundServiceCommand.STOP_SOUND_CIRCLE_DRAW -> {
                circleDrawSound.pause(); circleDrawSound.seekTo(0)
            }

            SoundServiceCommand.PLAY_SOUND_CIRCLE_FAILED -> circleFailedSound.start()
            SoundServiceCommand.PLAY_SOUND_CIRCLE_SUCCESS -> circleSuccessSound.start()
            SoundServiceCommand.PLAY_SOUND_LOOSE -> looseSound.start()
            SoundServiceCommand.PLAY_SOUND_WIN -> winSound.start()

            else -> Log.w("SoundService", "Unexpected SoundService command: $commandStr")
        }

        return Service.START_STICKY
    }

    private fun getSoundPlayers(): List<MediaPlayer> {
        return listOf(buttonSound, circleDrawSound, circleFailedSound, circleSuccessSound, looseSound, winSound)
    }

    private fun updateSoundVolume() {
        getSoundPlayers().forEach {
            it.setVolume(soundVolume, soundVolume)
        }
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

        getSoundPlayers().forEach { soundPlayer ->
            soundPlayer.stop()
            soundPlayer.release()
        }
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
    PLAY_SOUND_BUTTON,

    PLAY_SOUND_CIRCLE_DRAW,
    STOP_SOUND_CIRCLE_DRAW,

    PLAY_SOUND_CIRCLE_FAILED,
    PLAY_SOUND_CIRCLE_SUCCESS,
    PLAY_SOUND_LOOSE,
    PLAY_SOUND_WIN
}
