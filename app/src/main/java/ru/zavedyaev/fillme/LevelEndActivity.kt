package ru.zavedyaev.fillme

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import ru.zavedyaev.fillme.level.LevelPackProgress
import ru.zavedyaev.fillme.level.LevelStatus
import ru.zavedyaev.fillme.level.LevelsPacks
import ru.zavedyaev.fillme.level.ProgressInstance

class LevelEndActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_end)

        val levelPackId = intent.getIntExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, 0)
        val levelId = intent.getIntExtra(GameActivity.LEVEL_ID_EXTRA_NAME, 0)

        val levelPack = LevelsPacks.packs[levelPackId]!!
        val winConditions = levelPack.levels[levelId]!!.winConditions
        val levelPackProgress =
            ProgressInstance.progress.levelPackProgress.getOrPut(levelPackId) { LevelPackProgress(hashMapOf()) }
        val status = levelPackProgress.levelProgress.getOrPut(levelId) { LevelStatus(0) }

        findViewById<TextView>(R.id.levelName).text = resources.getString(R.string.level_name, levelId)

        val levelRating1 = findViewById<MaterialRatingBar>(R.id.levelRating1)
        if (status.starsCount > 0) {
            levelRating1.rating = 1f
        } else {
            levelRating1.rating = 0f
        }

        val levelRating2 = findViewById<MaterialRatingBar>(R.id.levelRating2)
        if (status.starsCount > 1) {
            levelRating2.rating = 1f
        } else {
            levelRating2.rating = 0f
        }

        val levelRating3 = findViewById<MaterialRatingBar>(R.id.levelRating3)
        if (status.starsCount > 2) {
            levelRating3.rating = 1f
        } else {
            levelRating3.rating = 0f
        }

        findViewById<TextView>(R.id.levelRating1MinSquare).text =
                resources.getString(R.string.min_percentage, (winConditions.oneStar.minSquare * 100).toInt())
        findViewById<TextView>(R.id.levelRating2MinSquare).text =
                resources.getString(R.string.min_percentage, (winConditions.twoStars.minSquare * 100).toInt())
        findViewById<TextView>(R.id.levelRating3MinSquare).text =
                resources.getString(R.string.min_percentage, (winConditions.threeStars.minSquare * 100).toInt())

        findViewById<TextView>(R.id.levelRating1MaxCircles).text =
                resources.getString(R.string.max_circles_count, winConditions.oneStar.maxCirclesCount)
        findViewById<TextView>(R.id.levelRating2MaxCircles).text =
                resources.getString(R.string.max_circles_count, winConditions.twoStars.maxCirclesCount)
        findViewById<TextView>(R.id.levelRating3MaxCircles).text =
                resources.getString(R.string.max_circles_count, winConditions.threeStars.maxCirclesCount)

        findViewById<MaterialButton>(R.id.restartButton).setOnClickListener {
            val i = Intent(this, GameActivity::class.java)
            i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, levelPackId)
            i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, levelId)
            startActivity(i)
        }

        //todo think about restrictions by stars count
        val nextLevelButton = findViewById<MaterialButton>(R.id.nextLevelButton)
        if (levelPack.levels.size > levelId + 1) {
            nextLevelButton.setOnClickListener {
                val i = Intent(this, GameActivity::class.java)
                i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, levelPackId)
                i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, levelId + 1)
                startActivity(i)
            }
        } else if (LevelsPacks.packs[levelPackId + 1] != null) {
            nextLevelButton.setOnClickListener {
                val i = Intent(this, GameActivity::class.java)
                i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, levelPackId + 1)
                i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, 0)
                startActivity(i)
            }
        } else {
            nextLevelButton.isEnabled = false
        }

        findViewById<MaterialButton>(R.id.levelSelectButton).setOnClickListener {
            val i = Intent(this, LevelSelectActivity::class.java)
            startActivity(i)
        }
    }

}
