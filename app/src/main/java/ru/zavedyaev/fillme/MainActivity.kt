package ru.zavedyaev.fillme

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.button.MaterialButton
import ru.zavedyaev.fillme.level.ProgressInstance

class MainActivity : BackgroundSoundActivity() {
    private lateinit var newGameButton: MaterialButton
    private lateinit var continueGameButton: MaterialButton
    private lateinit var levelSelectButton: MaterialButton
    private lateinit var settingsButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newGameButton = findViewById(R.id.newGameButton)
        continueGameButton = findViewById(R.id.continueButton)
        levelSelectButton = findViewById(R.id.levelSelectButton)
        settingsButton = findViewById(R.id.settingsButton)

        newGameButton.setOnClickListener {
            val i = Intent(this, GameActivity::class.java)
            i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, 0)
            i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, 0)
            startActivity(i)
        }

        levelSelectButton.setOnClickListener {
            val i = Intent(this, LevelSelectActivity::class.java)
            startActivity(i)
        }

        settingsButton.setOnClickListener {
            val i = Intent(this, SettingsActivity::class.java)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()

        val atLeastOneStar = ProgressInstance.getStarsCount() > 0
        if (atLeastOneStar) {
            newGameButton.visibility = View.GONE
            continueGameButton.visibility = View.VISIBLE
        } else {
            newGameButton.visibility = View.VISIBLE
            continueGameButton.visibility = View.GONE
        }

        val levelToContinue = ProgressInstance.getLevelToContinue()
        if (levelToContinue == null) {
            continueGameButton.visibility = View.GONE
        } else {
            continueGameButton.setOnClickListener {
                val i = Intent(this, GameActivity::class.java)
                i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, levelToContinue.levelPackId)
                i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, levelToContinue.levelId)
                startActivity(i)
            }
        }
    }

}
