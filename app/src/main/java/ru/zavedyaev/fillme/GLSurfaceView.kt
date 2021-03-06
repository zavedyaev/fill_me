package ru.zavedyaev.fillme

import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Build
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.TextView
import ru.zavedyaev.fillme.level.*
import ru.zavedyaev.fillme.primitive.Circle2D
import ru.zavedyaev.fillme.primitive.Point2D
import ru.zavedyaev.fillme.shader.TextureHelper
import java.util.*

class GLSurfaceView(
    context: Context,
    private val remainedCirclesCountView: TextView,
    private val currentSquareTextView: TextView,
    private val requiredSquareTextView: TextView,
    private val levelPackId: Int,
    private val levelId: Int,
    backgroundColorId: Int,
    private val showLevelEndActivity: (LevelEndStatus) -> Unit,

    private val playCircleDrawSound: () -> Unit,
    private val stopCircleDrawSound: () -> Unit,
    private val playCircleFailedSound: () -> Unit,
    private val playCircleSuccessSound: () -> Unit,
    private val playLooseSound: () -> Unit,
    private val playWin1Sound: () -> Unit,
    private val playWin2Sound: () -> Unit,
    private val playWin3Sound: () -> Unit
) : GLSurfaceView(context) {

    private val renderer: GLRenderer
    private val level: GameLevel
    private var status: LevelStatus
    private var winCondition: WinCondition

    private var previousX: Float? = null
    private var previousY: Float? = null
    private var previousTime: Long = 0
    private val oldSdk = Build.VERSION.SDK_INT <= 22
    private val checkIntersectionEveryStep = if (oldSdk) 100 else 5

    init {
        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(3)

        val defaultDisplay = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val rotation = defaultDisplay.rotation
        renderer = GLRenderer(context, rotation, levelPackId, levelId, backgroundColorId, 0)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
        val displayMetrics = DisplayMetrics()
        defaultDisplay.getMetrics(displayMetrics)

        // Render the view only when there is a change in the drawing data
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        level = LevelsPacks.packs.getValue(levelPackId).levels.getValue(levelId)
        status = ProgressInstance.getLevelStatus(levelPackId, levelId)
        winCondition = getCurrentWinCondition()


        remainedCirclesCountView.text =
                (winCondition.maxCirclesCount - renderer.circlesDrawn).toString()
        currentSquareTextView.text = resources.getString(R.string.filled_square_1, 0)
        requiredSquareTextView.text =
                resources.getString(R.string.filled_square_2, Math.round(winCondition.minSquare * 100))
    }


    fun getCirclesWithColors(): GameState = renderer.getCirclesWithColors()
    fun setCirclesWithColors(input: GameState) {
        if (input.circles.isEmpty()) return
        input.circles.forEachIndexed { index, circle ->
            renderer.addCircle(circle, input.colorIds[index])
        }

        renderer.circlesDrawn = input.circlesDrawn
        val remainedCirclesCount = winCondition.maxCirclesCount - renderer.circlesDrawn
        remainedCirclesCountView.text = (remainedCirclesCount).toString()

        val circlesSquare = renderer.calculateCirclesSquare()

        updateStatusIfNeeded(circlesSquare)
        winCondition = getCurrentWinCondition()

        currentSquareTextView.text = resources.getString(R.string.filled_square_1, Math.round(Math.floor(circlesSquare.toDouble() * 100)))
        requiredSquareTextView.text =
                resources.getString(R.string.filled_square_2, Math.round(winCondition.minSquare * 100))

        requestRender()
    }

    private fun getCurrentWinCondition(): WinCondition {
        val starsCount = status.starsCount

        return when (starsCount) {
            3 -> level.winConditions.threeStars
            2 -> level.winConditions.threeStars
            1 -> level.winConditions.twoStars
            else -> level.winConditions.oneStar
        }
    }

    private fun getCurrentStarsCount(currentCirclesSquare: Float): Int {
        val circlesDrawn = renderer.circlesDrawn

        return if (circlesDrawn <= level.winConditions.oneStar.maxCirclesCount && currentCirclesSquare >= level.winConditions.oneStar.minSquare) {
            if (circlesDrawn <= level.winConditions.twoStars.maxCirclesCount && currentCirclesSquare >= level.winConditions.twoStars.minSquare) {
                if (circlesDrawn <= level.winConditions.threeStars.maxCirclesCount && currentCirclesSquare >= level.winConditions.threeStars.minSquare) {
                    3
                } else {
                    2
                }
            } else {
                1
            }
        } else {
            0
        }
    }

    private fun updateStatusIfNeeded(currentCirclesSquare: Float) {
        val currentStarsCount = getCurrentStarsCount(currentCirclesSquare)

        if (currentStarsCount > status.starsCount) {
            status = ProgressInstance.updateStarsCount(levelPackId, levelId, currentStarsCount, context)
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        val x: Float = e.x
        val y: Float = e.y

        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                if (previousX == null && previousY == null && renderer.circlesDrawn < winCondition.maxCirclesCount) {
                    playCircleDrawSound()

                    previousX = x
                    previousY = y
                    previousTime = Date().time

                    val point = renderer.transformDisplayCoordinates(x, y)
                    renderer.drawingCircleFailed = renderer.drawingCircleIntersectsLevelMap()
                    renderer.drawingCircleTextureId = TextureHelper.getRandomCircleTextureId()
                    renderer.drawingCircle = Circle2D(point, MIN_RADIUS)
                    renderer.circlesDrawn++

                    val remainedCirclesCount = winCondition.maxCirclesCount - renderer.circlesDrawn

                    remainedCirclesCountView.text = (remainedCirclesCount).toString()

                    increaseThread = Thread(increaseRadiusRunnable)
                    increaseThread?.start()

                    requestRender()
                    return true
                } else {
                    return true //needed to register that ACTION_DOWN was processed and we are listening for ACTION_UP
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                increaseThread?.interrupt()
                stopCircleDrawSound()

                val intersect = renderer.drawingCircleIntersectsLevelMap()

                if (!intersect) {
                    renderer.drawingCircle?.let {
                        renderer.addCircle(it, renderer.drawingCircleTextureId)
                    }
                }

                renderer.drawingCircle = null
                renderer.drawingCircleFailed = false

                previousX = null
                previousY = null

                val circlesSquare = renderer.calculateCirclesSquare()

                updateStatusIfNeeded(circlesSquare)
                winCondition = getCurrentWinCondition()

                currentSquareTextView.text =
                        resources.getString(R.string.filled_square_1, Math.round(Math.floor(circlesSquare.toDouble() * 100)))
                requiredSquareTextView.text =
                        resources.getString(R.string.filled_square_2, Math.round(winCondition.minSquare * 100))

                requestRender()

                val currentStarsCount = getCurrentStarsCount(circlesSquare)
                if (currentStarsCount == 3) {
                    playWin3Sound()
                    showLevelEndActivity(LevelEndStatus.WON_3)
                    return true
                }

                if (renderer.circlesDrawn >= winCondition.maxCirclesCount) {
                    if (currentStarsCount == 2) {
                        playWin2Sound()
                        showLevelEndActivity(LevelEndStatus.WON_2)
                        return true
                    }
                    if (currentStarsCount == 1) {
                        playWin1Sound()
                        showLevelEndActivity(LevelEndStatus.WON_1)
                        return true
                    }

                    playLooseSound()
                    showLevelEndActivity(LevelEndStatus.LOST)
                    return true
                }

                if (!intersect) { //play these sounds only in case level is not finished
                    playCircleSuccessSound()
                } else {
                    playCircleFailedSound()
                }

                return true
            }
        }



        return super.onTouchEvent(e)
    }

    private var increaseThread: Thread? = null
    private val increaseRadiusRunnable = {
        var remainedStepsToCheckIntersection = checkIntersectionEveryStep
        while (true) {
            try {
                Thread.sleep(30)
            } catch (e: InterruptedException) {
                break
            }

            remainedStepsToCheckIntersection--

            val currentTime = Date().time
            val drawingCircle = renderer.drawingCircle ?: continue

            renderer.drawingCircle = Circle2D(
                drawingCircle.center as Point2D,
                drawingCircle.radius * Math.pow(
                    getRadiusMultiplier(drawingCircle.radius),
                    (currentTime - previousTime).toDouble()
                ).toFloat()
            )

            if (remainedStepsToCheckIntersection <= 0) {
                remainedStepsToCheckIntersection = checkIntersectionEveryStep
                renderer.drawingCircleFailed = renderer.drawingCircleIntersectsLevelMap()
            }

            previousTime = currentTime
            requestRender()
        }
    }

    companion object {
        private const val MAX_POSSIBLE_RADIUS = 25f
        private const val MIN_RADIUS = 0.1f

        private const val RADIUS_MULTIPLIER_MAX = 1.0028
        private const val RADIUS_MULTIPLIER_MIN = 1.0011
        private const val HUNDRED_PERCENT_MULTIPLIER_CHANGE = RADIUS_MULTIPLIER_MAX - RADIUS_MULTIPLIER_MIN

        private const val MIN_RADIUS_TO_CHANGE_MULTIPLIER = 0f
        private const val MAX_RADIUS_TO_CHANGE_MULTIPLIER = 3.3f
        private const val HUNDRED_PERCENT_RADIUS_CHANGE =
            MAX_RADIUS_TO_CHANGE_MULTIPLIER - MIN_RADIUS_TO_CHANGE_MULTIPLIER

        fun getRadiusMultiplier(radius: Float): Double {
            if (radius > MAX_POSSIBLE_RADIUS) return 1.0
            if (radius > MAX_RADIUS_TO_CHANGE_MULTIPLIER) return RADIUS_MULTIPLIER_MIN
            if (radius < MIN_RADIUS_TO_CHANGE_MULTIPLIER) return RADIUS_MULTIPLIER_MAX

            val currentRadiusValue = radius - MIN_RADIUS_TO_CHANGE_MULTIPLIER
            val radiusChangeInPercent = currentRadiusValue / HUNDRED_PERCENT_RADIUS_CHANGE

            val multiplierChange = HUNDRED_PERCENT_MULTIPLIER_CHANGE * radiusChangeInPercent

            return RADIUS_MULTIPLIER_MAX - multiplierChange
        }
    }
}