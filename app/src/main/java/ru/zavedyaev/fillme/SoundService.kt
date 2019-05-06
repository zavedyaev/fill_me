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
    private var backgroundMusicType: BackgroundMusicType = BackgroundMusicType.MENU
    private var backgroundMenuMusic: MediaPlayer? = null
    private var backgroundMainMusic: MediaPlayer? = null
    private lateinit var buttonSound: MediaPlayer
    private lateinit var circleDrawSound: MediaPlayer
    private lateinit var circleFailedSound: MediaPlayer
    private lateinit var circleSuccessSound: MediaPlayer
    private lateinit var looseSound: MediaPlayer
    private lateinit var win1Sound: MediaPlayer
    private lateinit var win2Sound: MediaPlayer
    private lateinit var win3Sound: MediaPlayer

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

        buttonSound = MediaPlayer.create(this@SoundService, R.raw.click)
        circleDrawSound = MediaPlayer.create(this@SoundService, R.raw.circle_inflate)
        circleFailedSound = MediaPlayer.create(this@SoundService, R.raw.circle_bang)
        circleSuccessSound = MediaPlayer.create(this@SoundService, R.raw.circle_placed)
        looseSound = MediaPlayer.create(this@SoundService, R.raw.lose)
        win1Sound = MediaPlayer.create(this@SoundService, R.raw.win_1_star)
        win2Sound = MediaPlayer.create(this@SoundService, R.raw.win_2_star)
        win3Sound = MediaPlayer.create(this@SoundService, R.raw.win_3_star)

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
            SoundServiceCommand.MAIN_MUSIC -> {
                switchBackgroundMusic(BackgroundMusicType.MAIN)
            }
            SoundServiceCommand.MENU_MUSIC -> {
                switchBackgroundMusic(BackgroundMusicType.MENU)
            }

            SoundServiceCommand.BACKGROUND_MUSIC_VOLUME_0 -> {
                backgroundMusicVolume = 0f
                backgroundMenuMusic?.setVolume(backgroundMusicVolume, backgroundMusicVolume)
                backgroundMainMusic?.setVolume(backgroundMusicVolume, backgroundMusicVolume)
            }
            SoundServiceCommand.BACKGROUND_MUSIC_VOLUME_1 -> {
                backgroundMusicVolume = 1f
                backgroundMenuMusic?.setVolume(backgroundMusicVolume, backgroundMusicVolume)
                backgroundMainMusic?.setVolume(backgroundMusicVolume, backgroundMusicVolume)
            }
            SoundServiceCommand.SOUND_VOLUME_0 -> {
                soundVolume = 0f
                updateSoundVolume()
            }
            SoundServiceCommand.SOUND_VOLUME_1 -> {
                soundVolume = 1f
                updateSoundVolume()
            }
            SoundServiceCommand.PLAY_SOUND_BUTTON -> buttonSound.restart()

            SoundServiceCommand.PLAY_SOUND_CIRCLE_DRAW -> circleDrawSound.start()
            SoundServiceCommand.STOP_SOUND_CIRCLE_DRAW -> {
                circleDrawSound.pause(); circleDrawSound.seekTo(0)
            }

            SoundServiceCommand.PLAY_SOUND_CIRCLE_FAILED -> circleFailedSound.restart()
            SoundServiceCommand.PLAY_SOUND_CIRCLE_SUCCESS -> circleSuccessSound.restart()
            SoundServiceCommand.PLAY_SOUND_LOOSE -> looseSound.restart()
            SoundServiceCommand.PLAY_SOUND_WIN_1 -> win1Sound.restart()
            SoundServiceCommand.PLAY_SOUND_WIN_2 -> win2Sound.restart()
            SoundServiceCommand.PLAY_SOUND_WIN_3 -> win3Sound.restart()

            else -> Log.w("SoundService", "Unexpected SoundService command: $commandStr")
        }

        return Service.START_STICKY
    }

    private fun switchBackgroundMusic(newBackgroundMusicType: BackgroundMusicType) {
        backgroundMusicType = newBackgroundMusicType

        when(newBackgroundMusicType) {
            BackgroundMusicType.MAIN -> {
                val currentBackgroundMenuMusic = backgroundMenuMusic
                if (currentBackgroundMenuMusic != null) {
                    if (currentBackgroundMenuMusic.isPlaying) {
                        currentBackgroundMenuMusic.pause()
                        currentBackgroundMenuMusic.seekTo(0)
                    }
                }

                val currentBackgroundMainMusic = backgroundMainMusic
                if (currentBackgroundMainMusic != null) {
                    if (!currentBackgroundMainMusic.isPlaying) currentBackgroundMainMusic.start()
                }
            }
            BackgroundMusicType.MENU -> {
                val currentBackgroundMainMusic = backgroundMainMusic
                if (currentBackgroundMainMusic != null) {
                    if (currentBackgroundMainMusic.isPlaying) {
                        currentBackgroundMainMusic.pause()
                        currentBackgroundMainMusic.seekTo(0)
                    }
                }

                val currentBackgroundMenuMusic = backgroundMenuMusic
                if (currentBackgroundMenuMusic != null) {
                    if (!currentBackgroundMenuMusic.isPlaying) currentBackgroundMenuMusic.start()
                }
            }
        }

    }

    private fun getSoundPlayers(): List<MediaPlayer> {
        return listOf(buttonSound, circleDrawSound, circleFailedSound, circleSuccessSound, looseSound, win1Sound, win2Sound, win3Sound)
    }

    private fun updateSoundVolume() {
        getSoundPlayers().forEach {
            it.setVolume(soundVolume, soundVolume)
        }
        circleDrawSound.setVolume(soundVolume*0.5f, soundVolume*0.5f)
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
                if (backgroundMenuMusic == null) {
                    val newBackgroundMusic = MediaPlayer.create(this@SoundService, R.raw.menu_theme)
                    backgroundMenuMusic = newBackgroundMusic
                    newBackgroundMusic.isLooping = true
                    newBackgroundMusic.setVolume(backgroundMusicVolume, backgroundMusicVolume)
                    if (backgroundMusicType == BackgroundMusicType.MENU) newBackgroundMusic.start()
                }
                if (backgroundMainMusic == null) {
                    val newBackgroundMusic = MediaPlayer.create(this@SoundService, R.raw.main_theme)
                    backgroundMainMusic = newBackgroundMusic
                    newBackgroundMusic.isLooping = true
                    newBackgroundMusic.setVolume(backgroundMusicVolume, backgroundMusicVolume)
                    if (backgroundMusicType == BackgroundMusicType.MAIN) newBackgroundMusic.start()
                }
            }
        } catch (e: InterruptedException) {
            //do nothing
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundMenuMusic?.stop()
        backgroundMenuMusic?.release()
        backgroundMenuMusic = null

        backgroundMainMusic?.stop()
        backgroundMainMusic?.release()
        backgroundMainMusic = null

        getSoundPlayers().forEach { soundPlayer ->
            soundPlayer.stop()
            soundPlayer.release()
        }
    }

    companion object {
        const val COMMAND_EXTRA_NAME = "COMMAND_EXTRA"
    }
}

fun MediaPlayer.restart() {
    if (isPlaying) {
        pause()
        seekTo(0)
    }
    start()
}


enum class SoundServiceCommand {
    /** Should  */
    PAUSE,
    RESUME,
    MENU_MUSIC,
    MAIN_MUSIC,
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
    PLAY_SOUND_WIN_1,
    PLAY_SOUND_WIN_2,
    PLAY_SOUND_WIN_3
}

enum class BackgroundMusicType {
    MENU,
    MAIN
}
