package ru.zavedyaev.fillme

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.TextView
import ru.zavedyaev.fillme.level.*
import ru.zavedyaev.fillme.primitive.Circle2D
import ru.zavedyaev.fillme.primitive.Point2D
import java.util.*

class GLSurfaceView(
    context: Context,
    private val remainedCirclesCountView: TextView,
    private val squareTextView: TextView,
    private val levelPackId: Int,
    private val levelId: Int,
    private val showLevelEndActivity: () -> Unit
) : GLSurfaceView(context) {

    private val renderer: GLRenderer
    private val level: GameLevel
    private var status: LevelStatus
    private var winCondition: WinCondition

    private var previousX: Float? = null
    private var previousY: Float? = null
    private var previousTime: Long = 0

    init {
        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(3)

        val defaultDisplay = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val rotation = defaultDisplay.rotation
        renderer = GLRenderer(rotation, levelPackId, levelId)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
        val displayMetrics = DisplayMetrics()
        defaultDisplay.getMetrics(displayMetrics)

        // Render the view only when there is a change in the drawing data
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        level = LevelsPacks.packs[levelPackId]!!.levels[levelId]!!
        status = ProgressInstance.getLevelStatus(levelPackId, levelId)
        winCondition = getCurrentWinCondition()


        remainedCirclesCountView.text =
                (winCondition.maxCirclesCount - renderer.circlesDrawn).toString()
        squareTextView.text = resources.getString(
            R.string.filled_square,
            0,
            Math.round(winCondition.minSquare * 100)
        )
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
                    previousX = x
                    previousY = y
                    previousTime = Date().time

                    val point = renderer.transformDisplayCoordinates(x, y)
                    renderer.drawingCircle = Circle2D(point, 0.1f)
                    renderer.circlesDrawn++

                    val remainedCirclesCount = winCondition.maxCirclesCount - renderer.circlesDrawn

                    remainedCirclesCountView.text = (remainedCirclesCount).toString()

                    increaseThread = Thread(increaseRadiusRunnable)
                    increaseThread?.start()

                    requestRender()
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                increaseThread?.interrupt()

                val intersect = renderer.drawingCircleIntersectsLevelMap()

                if (!intersect) {
                    renderer.drawingCircle?.let { renderer.addCircle(it) }
                }

                renderer.drawingCircle = null

                previousX = null
                previousY = null

                val circlesSquare = renderer.calculateCirclesSquare()

                updateStatusIfNeeded(circlesSquare)
                winCondition = getCurrentWinCondition()

                squareTextView.text = resources.getString(
                    R.string.filled_square,
                    Math.round(circlesSquare * 100),
                    Math.round(winCondition.minSquare * 100)
                )
                requestRender()

                if (getCurrentStarsCount(circlesSquare) == 3 || renderer.circlesDrawn >= winCondition.maxCirclesCount) {
                    showLevelEndActivity()
                }

                return true
            }
        }



        return super.onTouchEvent(e)
    }

    private var increaseThread: Thread? = null
    private val increaseRadiusRunnable = {
        while (true) {
            try {
                Thread.sleep(30)
            } catch (e: InterruptedException) {
                break
            }

            val currentTime = Date().time
            val drawingCircle = renderer.drawingCircle ?: continue

            renderer.drawingCircle = Circle2D(
                drawingCircle.center as Point2D,
                drawingCircle.radius * Math.pow(1.0025, (currentTime - previousTime).toDouble()).toFloat()
            )
            previousTime = currentTime
            requestRender()
        }
    }
}