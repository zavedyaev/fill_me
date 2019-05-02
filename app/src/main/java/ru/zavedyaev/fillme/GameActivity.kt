package ru.zavedyaev.fillme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ru.zavedyaev.fillme.shader.TextureHelper

class GameActivity : BackgroundSoundActivity() {

    private lateinit var gLView: GLSurfaceView
    private var backgroundColorId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_layout)

        val levelPackId = intent.getIntExtra(LEVEL_PACK_ID_EXTRA_NAME, 0)
        val levelId = intent.getIntExtra(LEVEL_ID_EXTRA_NAME, 0)

        val remainedCirclesCountView = findViewById<TextView>(R.id.circlesTextView)
        val currentSquareTextView = findViewById<TextView>(R.id.currentSquareTextView)
        val requiredSquareTextView = findViewById<TextView>(R.id.requiredSquareTextView)

        val showLevelEndActivity = { levelEndStatus: LevelEndStatus ->
            val i = Intent(this, LevelEndActivity::class.java)
            i.putExtra(LEVEL_PACK_ID_EXTRA_NAME, levelPackId)
            i.putExtra(LEVEL_ID_EXTRA_NAME, levelId)
            i.putExtra(LEVEL_END_STATUS_EXTRA_NAME, levelEndStatus.name)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        backgroundColorId = savedInstanceState?.getInt(STATE_BACKGROUND_ID_KEY) ?: TextureHelper.getRandomBackgroundId()

        gLView = GLSurfaceView(
            this,
            remainedCirclesCountView,
            currentSquareTextView,
            requiredSquareTextView,
            levelPackId,
            levelId,
            backgroundColorId,
            showLevelEndActivity,
            playCircleDrawSound,
            stopCircleDrawSound,
            playCircleFailedSound,
            playCircleSuccessSound,
            playLooseSound,
            playWinSound
        )

        if (savedInstanceState != null) {
            val serialized = savedInstanceState.getString(STATE_CIRCLES_KEY)
            val circles = mapper.readValue(serialized, GameState::class.java)
            gLView.setCirclesWithColors(circles)
        }

        val layout: RelativeLayout = findViewById(R.id.GameLayout)
        layout.removeView(layout.findViewById<View>(R.id.GameView))
        layout.addView(gLView, 0)

        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            playButtonSound()
            showLevelEndActivity(LevelEndStatus.PAUSE)
        }

        findViewById<ImageButton>(R.id.restartButton).setOnClickListener {
            playButtonSound()
            val i = Intent(this, GameActivity::class.java)
            i.putExtra(LEVEL_PACK_ID_EXTRA_NAME, levelPackId)
            i.putExtra(LEVEL_ID_EXTRA_NAME, levelId)
            startActivity(i)
        }
    }

    override fun onBackPressed() {
        val i = Intent(this, LevelSelectActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(STATE_BACKGROUND_ID_KEY, backgroundColorId)
        val serializedCircles = mapper.writeValueAsString(gLView.getCirclesWithColors())
        outState.putString(STATE_CIRCLES_KEY, serializedCircles)
    }

    override fun onResume() {
        super.onResume()
        gLView.onResume()
    }

    override fun onPause() {
        super.onPause()
        gLView.onPause()
    }

    private fun sendSoundCommand(command: SoundServiceCommand) {
        val serviceIntent = Intent(this, SoundService::class.java)
        serviceIntent.putExtra(SoundService.COMMAND_EXTRA_NAME, command.name)
        startService(serviceIntent)
    }

    private val playCircleDrawSound = { sendSoundCommand(SoundServiceCommand.PLAY_SOUND_CIRCLE_DRAW) }
    private val stopCircleDrawSound = { sendSoundCommand(SoundServiceCommand.STOP_SOUND_CIRCLE_DRAW) }
    private val playCircleFailedSound = { sendSoundCommand(SoundServiceCommand.PLAY_SOUND_CIRCLE_FAILED) }
    private val playCircleSuccessSound = { sendSoundCommand(SoundServiceCommand.PLAY_SOUND_CIRCLE_SUCCESS) }
    private val playLooseSound = { sendSoundCommand(SoundServiceCommand.PLAY_SOUND_LOOSE) }
    private val playWinSound = { sendSoundCommand(SoundServiceCommand.PLAY_SOUND_WIN) }

    companion object {
        const val LEVEL_PACK_ID_EXTRA_NAME = "LEVEL_PACK_ID"
        const val LEVEL_ID_EXTRA_NAME = "LEVEL_ID"
        const val LEVEL_END_STATUS_EXTRA_NAME = "LEVEL_END_STATUS"

        const val STATE_BACKGROUND_ID_KEY = "STATE_BACKGROUND_ID_KEY"
        const val STATE_CIRCLES_KEY = "STATE_CIRCLES_KEY"

        val mapper = jacksonObjectMapper()
    }
}
