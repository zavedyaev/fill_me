package ru.zavedyaev.fillme

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import ru.zavedyaev.fillme.level.ProgressInstance

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val atLeastOneStar = ProgressInstance.progress.getStarsCount() > 0

        val newGameButton = findViewById<MaterialButton>(R.id.newGameButton)
        val continueGameButton = findViewById<MaterialButton>(R.id.continueButton)

        if (atLeastOneStar) {
            newGameButton.visibility = View.GONE
            continueGameButton.visibility = View.VISIBLE
        } else {
            newGameButton.visibility = View.VISIBLE
            continueGameButton.visibility = View.GONE
        }

        newGameButton.setOnClickListener {
            val i = Intent(this, GameActivity::class.java)
            i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, 0)
            i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, 0)
            startActivity(i)
        }

        continueGameButton.setOnClickListener {
            val lastLevelPackId = ProgressInstance.progress.levelPackProgress.keys.max() ?: 0
            val lastLevelId =
                ProgressInstance.progress.levelPackProgress[lastLevelPackId]?.levelProgress?.keys?.max() ?: 0

            val lastLevelStarsCount =
                ProgressInstance.progress.levelPackProgress[lastLevelPackId]?.levelProgress?.get(lastLevelId)?.starsCount
                    ?: 0

            val i = Intent(this, GameActivity::class.java)
            if (lastLevelStarsCount < 3) {
                i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, lastLevelPackId)
                i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, lastLevelId)
            } else {
                //todo think about restrictions
                i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, lastLevelPackId)
                i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, lastLevelId + 1)
            }

            startActivity(i)
        }

        findViewById<MaterialButton>(R.id.levelSelectButton).setOnClickListener {
            val i = Intent(this, LevelSelectActivity::class.java)
            startActivity(i)
        }
    }

}