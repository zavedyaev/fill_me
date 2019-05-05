package ru.zavedyaev.fillme

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import ru.zavedyaev.fillme.level.LevelsPacks
import ru.zavedyaev.fillme.level.ProgressInstance

class LevelEndActivity : BackgroundSoundActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_end)

        val levelPackId = intent.getIntExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, 0)
        val levelId = intent.getIntExtra(GameActivity.LEVEL_ID_EXTRA_NAME, 0)
        val levelEndStatus = LevelEndStatus.valueOf(intent.getStringExtra(GameActivity.LEVEL_END_STATUS_EXTRA_NAME))

        val levelPack = LevelsPacks.packs[levelPackId]!!
        val winConditions = levelPack.levels[levelId]!!.winConditions
        val status = ProgressInstance.getLevelStatus(levelPackId, levelId)

        val title = when (levelEndStatus) {
            LevelEndStatus.PAUSE -> resources.getString(R.string.level_name_pause, levelId)
            LevelEndStatus.WON_1, LevelEndStatus.WON_2, LevelEndStatus.WON_3 -> resources.getString(
                R.string.level_name_won,
                levelId
            )
            LevelEndStatus.LOST -> resources.getString(R.string.level_name_lost, levelId)
        }

        findViewById<LinearLayout>(R.id.levelEndMainLayout).setOnClickListener {
            playButtonSound()
            this.onBackPressed()
        }
        findViewById<MaterialCardView>(R.id.levelEndCardView).setOnClickListener { }

        findViewById<TextView>(R.id.levelName).text = title

        LevelDescriptionHelper.fillDescription(
            { id: Int -> findViewById(id) },
            { id: Int -> findViewById(id) },
            resources,
            status.starsCount,
            winConditions
        )

        findViewById<MaterialButton>(R.id.restartButton).setOnClickListener {
            playButtonSound()
            val i = Intent(this, GameActivity::class.java)
            i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, levelPackId)
            i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, levelId)
            startActivity(i)
        }

        val nextLevelButton = findViewById<MaterialButton>(R.id.nextLevelButton)
        val starsCount = ProgressInstance.getStarsCount()
        if (levelPack.levels.size > levelId + 1) {
            val nextLevel = LevelsPacks.packs[levelPackId]!!.levels[levelId + 1]!!
            if (nextLevel.starsToUnlock <= starsCount) {
                nextLevelButton.setOnClickListener {
                    playButtonSound()
                    val i = Intent(this, GameActivity::class.java)
                    i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, levelPackId)
                    i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, levelId + 1)
                    startActivity(i)
                }
            } else {
                nextLevelButton.isEnabled = false
            }
        } else if (LevelsPacks.packs[levelPackId + 1] != null) {
            val nextLevel = LevelsPacks.packs[levelPackId + 1]!!.levels[0]!!
            if (nextLevel.starsToUnlock <= starsCount) {
                nextLevelButton.setOnClickListener {
                    playButtonSound()
                    val i = Intent(this, GameActivity::class.java)
                    i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, levelPackId + 1)
                    i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, 0)
                    startActivity(i)
                }
            } else {
                nextLevelButton.isEnabled = false
            }
        } else {
            nextLevelButton.isEnabled = false
        }

        findViewById<MaterialButton>(R.id.levelSelectButton).setOnClickListener {
            playButtonSound()
            val i = Intent(this, LevelSelectActivity::class.java)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        mainBackgroundMusic()
    }
}

enum class LevelEndStatus {
    PAUSE, WON_1, WON_2, WON_3, LOST
}
