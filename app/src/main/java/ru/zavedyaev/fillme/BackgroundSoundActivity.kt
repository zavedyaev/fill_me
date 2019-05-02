package ru.zavedyaev.fillme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

abstract class BackgroundSoundActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        val serviceIntent = Intent(this, SoundService::class.java)
        serviceIntent.putExtra(SoundService.COMMAND_EXTRA_NAME, SoundServiceCommand.RESUME.name)
        startService(serviceIntent)
    }

    override fun onPause() {
        super.onPause()
        val serviceIntent = Intent(this, SoundService::class.java)
        serviceIntent.putExtra(SoundService.COMMAND_EXTRA_NAME, SoundServiceCommand.PAUSE.name)
        startService(serviceIntent)
    }

    fun playButtonSound() {
        val serviceIntent = Intent(this, SoundService::class.java)
        serviceIntent.putExtra(SoundService.COMMAND_EXTRA_NAME, SoundServiceCommand.PLAY_SOUND_BUTTON.name)
        startService(serviceIntent)
    }
}