package ru.zavedyaev.fillme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var gLView: MyGLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_layout)

        val levelPackId = intent.getIntExtra(LEVEL_PACK_ID_EXTRA_NAME, 0)
        val levelId = intent.getIntExtra(LEVEL_ID_EXTRA_NAME, 0)

        val remainedCirclesCountView = findViewById<TextView>(R.id.circlesTextView)
        val squareTextView = findViewById<TextView>(R.id.squareTextView)

        val showLevelEndActivity = {
            val i = Intent(this, LevelEndActivity::class.java)
            i.putExtra(LEVEL_PACK_ID_EXTRA_NAME, levelPackId)
            i.putExtra(LEVEL_ID_EXTRA_NAME, levelId)
            startActivity(i)
        }

        gLView = MyGLSurfaceView(
            this,
            remainedCirclesCountView,
            squareTextView,
            levelPackId,
            levelId,
            showLevelEndActivity
        )

        val layout: RelativeLayout = findViewById(R.id.GameLayout)
        layout.removeView(layout.findViewById<View>(R.id.GameView))
        layout.addView(gLView, 0)

        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener { showLevelEndActivity() }

        findViewById<ImageButton>(R.id.restartButton).setOnClickListener {
            val i = Intent(this, GameActivity::class.java)
            i.putExtra(LEVEL_PACK_ID_EXTRA_NAME, levelPackId)
            i.putExtra(LEVEL_ID_EXTRA_NAME, levelId)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        gLView.onResume()
    }

    override fun onPause() {
        super.onPause()
        gLView.onPause()
    }

    companion object {
        const val LEVEL_PACK_ID_EXTRA_NAME = "LEVEL_PACK_ID"
        const val LEVEL_ID_EXTRA_NAME = "LEVEL_ID"
    }
}
