package ru.zavedyaev.fillme

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.zavedyaev.fillme.level.ProgressInstance

class SettingsActivity : BackgroundSoundActivity() {

    private lateinit var resetProgressButton: MaterialButton
    private lateinit var backgroundMusicSwitch: SwitchMaterial
    private lateinit var soundsSwitch: SwitchMaterial
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        resetProgressButton = findViewById(R.id.resetProgressButton)
        resetProgressButton.setOnClickListener {
            ProgressInstance.resetProgress(this)
            Snackbar.make(findViewById(R.id.root), R.string.progress_erased, Snackbar.LENGTH_SHORT).show()
        }

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        backgroundMusicSwitch = findViewById(R.id.backgroundMusicSwitch)
        backgroundMusicSwitch.isChecked = sharedPreferences.getFloat(SharedPreferencesKey.BACKGROUND_MUSIC_VOLUME.name, 1f) == 1f
        backgroundMusicSwitch.setOnClickListener {
            val checked = backgroundMusicSwitch.isChecked
            with (sharedPreferences.edit()) {
                putFloat(SharedPreferencesKey.BACKGROUND_MUSIC_VOLUME.name, if (checked) 1f else 0f )
                apply()
            }

            val serviceIntent = Intent(this, SoundService::class.java)
            serviceIntent.putExtra(SoundService.COMMAND_EXTRA_NAME, (if (checked) SoundServiceCommand.BACKGROUND_MUSIC_VOLUME_1 else SoundServiceCommand.BACKGROUND_MUSIC_VOLUME_0).name)
            startService(serviceIntent)
        }

        soundsSwitch = findViewById(R.id.soundsSwitch)
        soundsSwitch.isChecked = sharedPreferences.getFloat(SharedPreferencesKey.SOUND_VOLUME.name, 1f) == 1f
        soundsSwitch.setOnClickListener {
            val checked = soundsSwitch.isChecked
            with (sharedPreferences.edit()) {
                putFloat(SharedPreferencesKey.SOUND_VOLUME.name, if (checked) 1f else 0f )
                apply()
            }

            val serviceIntent = Intent(this, SoundService::class.java)
            serviceIntent.putExtra(SoundService.COMMAND_EXTRA_NAME, (if (checked) SoundServiceCommand.SOUND_VOLUME_1 else SoundServiceCommand.SOUND_VOLUME_0).name)
            startService(serviceIntent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //return to main activity
        return when (item?.itemId) {
            android.R.id.home -> {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
