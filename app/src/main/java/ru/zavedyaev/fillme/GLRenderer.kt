package ru.zavedyaev.fillme

import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.view.Surface
import ru.zavedyaev.fillme.level.Border2D
import ru.zavedyaev.fillme.level.LevelsPacks
import ru.zavedyaev.fillme.primitive.Circle2D
import ru.zavedyaev.fillme.primitive.Point2D
import ru.zavedyaev.fillme.shader.Shader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer(
    private val displayRotation: Int,
    levelPackId: Int,
    levelId: Int
) : GLSurfaceView.Renderer {

    val level = LevelsPacks.packs[levelPackId]!!.levels[levelId]!!
    private val levelMapSquare = calculateLevelMapSquare()
    private val levelFreeSquare = 1 - levelMapSquare

    // vpMatrix is an abbreviation for "Model View Projection Matrix"
    private val vpMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)

    private fun calcRotateSceneDegrees(displayRotation: Int) = when (displayRotation) {
        Surface.ROTATION_270 -> 90f
        Surface.ROTATION_180 -> 180f
        Surface.ROTATION_90 -> -90f
        else -> 0f
    }

    private val rotateSceneDegrees: Float = calcRotateSceneDegrees(displayRotation)

    private var programHandle: Int = -1


    private var displayHeightPixels: Int = 0
    private var displayWidthPixels: Int = 0
    private var displayRatio: Float = 0f

    private var displayMaxPixels: Int = 0
    private var displayMinPixels: Int = 0

    private val circles = ArrayList<Circle2D>()
    fun addCircle(circle: Circle2D) {
        synchronized(circles) {
            circles.add(circle)
        }
    }

    var circlesDrawn: Int = 0
    var drawingCircle: Circle2D? = null

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
//        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES31.glClearColor(1f, 1f, 1f, 1.0f)

        // Set the camera position (View matrix)
//        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
//        Matrix.setLookAtM(viewMatrix, 0, 4.5f, 7f, 3f, 4.5f, 7f, 0f, 0f, 1.0f, 0.0f)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        val vertexShader: Int = GLRenderer.loadShader(GLES31.GL_VERTEX_SHADER, Shader.vertexShaderCode)
        val fragmentShader: Int = GLRenderer.loadShader(GLES31.GL_FRAGMENT_SHADER, Shader.fragmentShaderCode)

        programHandle = createAndLinkProgram(vertexShader, fragmentShader)
    }

    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)


        // Calculate the projection and view transformation
        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        val rotatedMatrix = FloatArray(16)

        Matrix.setRotateM(rotationMatrix, 0, rotateSceneDegrees, 0f, 0f, -1.0f)

        // Combine the rotation matrix with the projection and camera view
        // Note that the vpMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(rotatedMatrix, 0, vpMatrix, 0, rotationMatrix, 0)

        GLES31.glUseProgram(programHandle)

        val positionHandle = GLES31.glGetAttribLocation(programHandle, "vPosition")
        val colorHandle = GLES31.glGetUniformLocation(programHandle, "vColor")
        val vpMatrixHandle = GLES31.glGetUniformLocation(programHandle, "uMVPMatrix")

        // Pass the projection and view transformation to the shader
        GLES31.glUniformMatrix4fv(vpMatrixHandle, 1, false, rotatedMatrix, 0)

        synchronized(circles) {
            circles.forEach { it.draw(positionHandle, colorHandle, floatArrayOf(1f, 0f, 0f, 1f)) }
        }
        Border2D.draw(positionHandle, colorHandle, floatArrayOf(0.5f, 0.5f, 0.5f, 0.5f))
        level.draw(positionHandle, colorHandle, floatArrayOf(0f, 0f, 1f, 1.0f))

        drawingCircle?.draw(positionHandle, colorHandle, floatArrayOf(1f, 0f, 0f, 1f))
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES31.glViewport(0, 0, width, height)

        displayHeightPixels = height
        displayWidthPixels = width

        displayMaxPixels = Math.max(displayWidthPixels, displayHeightPixels)
        displayMinPixels = Math.min(displayWidthPixels, displayHeightPixels)
        displayRatio = displayMaxPixels.toFloat() / displayMinPixels

        val left: Float
        val right: Float
        val bottom: Float
        val top: Float

        if (displayRatio > SCENE_RATIO) {
            //need to add blank regions with width = displayWidth
            val halfWidth = SCENE_WIDTH.toFloat() / 2
            left = -halfWidth
            right = halfWidth

            val displayLengthInCoordinates = SCENE_WIDTH * displayRatio
            val difference = displayLengthInCoordinates - SCENE_LENGTH

            val halfLength = (SCENE_LENGTH + difference) / 2

            bottom = -halfLength
            top = halfLength
        } else {
            //need to add blank regions with length = displayLength
            val halfLength = SCENE_LENGTH.toFloat() / 2
            bottom = -halfLength
            top = halfLength

            val displayWidthInCoordinates = SCENE_LENGTH / displayRatio
            val difference = displayWidthInCoordinates - SCENE_WIDTH
            val halfWidth = (SCENE_WIDTH + difference) / 2
            left = -halfWidth
            right = halfWidth
        }

        if (displayMaxPixels != height) {
            Matrix.frustumM(projectionMatrix, 0, bottom, top, left, right, 3f, 7f)
        } else {
            Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, 3f, 7f)
        }
    }

    fun transformDisplayCoordinates(x: Float, y: Float): Point2D {
        val rotatedX: Float
        val rotatedY: Float

        when (displayRotation) {
            Surface.ROTATION_270 -> {
                rotatedX = y
                rotatedY = x
            }
            Surface.ROTATION_180 -> {
                rotatedX = displayWidthPixels - x
                rotatedY = y
            }
            Surface.ROTATION_90 -> {
                rotatedX = displayHeightPixels - y
                rotatedY = displayWidthPixels - x
            }
            else -> {
                rotatedX = x
                rotatedY = displayHeightPixels - y
            }
        }

        val fixedX: Float
        val fixedY: Float

        if (displayRatio > SCENE_RATIO) {
            //need to add blank regions with width = displayWidth

            val pixelsPerSceneUnit = displayMinPixels.toFloat() / SCENE_WIDTH
            val sceneLengthInPixels = displayMinPixels * SCENE_RATIO
            val difference = displayMaxPixels - sceneLengthInPixels
            val differenceHalf = difference / 2

            fixedX = (rotatedX / pixelsPerSceneUnit) - (SCENE_WIDTH.toFloat() / 2)
            fixedY = ((rotatedY - differenceHalf) / pixelsPerSceneUnit) - (SCENE_LENGTH.toFloat() / 2)
        } else {
            //need to add blank regions with length = displayLength

            val pixelsPerSceneUnit = displayMaxPixels.toFloat() / SCENE_LENGTH
            val sceneWidthInPixels = displayMaxPixels / SCENE_RATIO
            val difference = displayMinPixels - sceneWidthInPixels
            val differenceHalf = difference / 2

            fixedX = ((rotatedX - differenceHalf) / pixelsPerSceneUnit) - (SCENE_WIDTH.toFloat() / 2)
            fixedY = (rotatedY / pixelsPerSceneUnit) - -(SCENE_LENGTH.toFloat() / 2)
        }

        return Point2D(fixedX, fixedY)
    }

    private fun calculateLevelMapSquare(): Float {
        val scale = 10
        val meshLength = SCENE_LENGTH * scale
        val meshWidth = SCENE_WIDTH * scale

        val meshSquare = (meshLength + 1) * (meshWidth + 1)

        var pointsInGameLevel = 0L

        (0..(meshLength)).forEach forEachY@{ y ->
            (0..(meshWidth)).forEach forEachX@{ x ->
                val point = Point2D((x.toFloat() / scale) - SCENE_WIDTH_HALF, (y.toFloat() / scale) - SCENE_LENGTH_HALF)
                if (level.containsPoint(point)) pointsInGameLevel++
            }
        }

        return pointsInGameLevel.toFloat() / meshSquare
    }

    fun calculateCirclesSquare(): Float {
        val scale = 10
        val meshLength = SCENE_LENGTH * scale
        val meshWidth = SCENE_WIDTH * scale

        val meshSquare = (meshLength + 1) * (meshWidth + 1)

        var pointsInCircles = 0L

        synchronized(circles) {
            (0..(meshLength)).forEach forEachY@{ y ->
                (0..(meshWidth)).forEach forEachX@{ x ->
                    val point =
                        Point2D((x.toFloat() / scale) - SCENE_WIDTH_HALF, (y.toFloat() / scale) - SCENE_LENGTH_HALF)
                    circles.forEach forEachCircle@{ circle ->
                        if (circle.containsPoint(point)) {
                            pointsInCircles++
                            return@forEachX
                        }
                    }
                }
            }
        }

        val squarePercentageFromScene = pointsInCircles.toFloat() / meshSquare
        return squarePercentageFromScene / levelFreeSquare
    }

    fun drawingCircleIntersectsLevelMap(): Boolean {
        val circle = drawingCircle ?: return false

        //step 1 check center is in triangles
        level.triangles.forEach { triangle ->
            if (triangle.containsPoint2D(circle.center as Point2D)) return true
        }

        //step 2 check lines which could intersect circle
        level.triangles.forEach { triangle ->
            triangle.lines.forEach forEachLine@{ line ->
                // 2D line by 2 points
                //    A          B              C
                // (y1-y2)x + (x2-x1)y + (x1*y2 - x2*y1) = 0
                val a = line.a.y - line.b.y
                val b = line.b.x - line.a.x
                val c = line.a.x * line.b.y - line.b.x * line.a.y

                // distance between circle center and line
                val h =
                    Math.abs(a * circle.center.x + b * circle.center.y + c) / Math.sqrt((a * a + b * b).toDouble()).toFloat()

                //cannot intersect
                if (h >= circle.radius) return@forEachLine

                // check what both points are located outside of line and that line is not inside circle
                if (Math.abs(b) > EPS) {
                    val temp = Math.sqrt(
                        Math.pow(
                            (2f * c * a - 2f * circle.center.x * (b * b) + 2f * a * b * circle.center.y).toDouble(),
                            2.0
                        ) - 4f * (a * a + b * b) * (c * c + 2f * c * b * circle.center.y + circle.center.x * circle.center.x * b * b + b * b * circle.center.y * circle.center.y - b * b * circle.radius * circle.radius)
                    ).toFloat()
                    val px1 =
                        (-temp - 2f * c * a + 2f * circle.center.x * b * b - 2f * a * b * circle.center.y) / (2 * (a * a + b * b))
                    val px2 =
                        (temp - 2f * c * a + 2f * circle.center.x * b * b - 2f * a * b * circle.center.y) / (2 * (a * a + b * b))

                    var fixedAX = line.a.x
                    var fixedBX = line.b.x
                    if (line.a.x > line.b.x) {
                        fixedAX = line.b.x
                        fixedBX = line.a.x
                    }

                    if ((px1 >= fixedAX || px2 >= fixedAX) && (fixedBX >= px1 || fixedBX >= px2)) {
                        return true
                    }

                } else {
                    val temp = Math.sqrt(
                        Math.pow(
                            (2f * c * b - 2f * circle.center.y * (a * a) + 2f * b * a * circle.center.x).toDouble(),
                            2.0
                        ) - 4f * (b * b + a * a) * (c * c + 2f * c * a * circle.center.x + circle.center.y * circle.center.y * a * a + a * a * circle.center.x * circle.center.x - a * a * circle.radius * circle.radius)
                    ).toFloat()
                    val py1 =
                        (-temp - 2f * c * b + 2f * circle.center.y * a * a - 2f * b * a * circle.center.x) / (2 * (b * b + a * a))
                    val py2 =
                        (temp - 2f * c * b + 2f * circle.center.y * a * a - 2f * b * a * circle.center.x) / (2 * (b * b + a * a))

                    var fixedAY = line.a.y
                    var fixedBY = line.b.y

                    if (fixedAY > fixedBY) {
                        fixedAY = line.b.y
                        fixedBY = line.a.y
                    }

                    if ((py1 >= fixedAY || py2 >= fixedAY) && (fixedBY >= py1 || fixedBY >= py2)) {
                        return true
                    }
                }
            }
        }

        return false
    }


    companion object {
        const val EPS = 0.00001f
        const val SCENE_LENGTH = 14
        const val SCENE_LENGTH_HALF = SCENE_LENGTH.toFloat() / 2
        const val SCENE_WIDTH = 9 // it could be height in horizontal mode
        const val SCENE_WIDTH_HALF = SCENE_WIDTH.toFloat() / 2
        const val SCENE_RATIO = SCENE_LENGTH.toFloat() / SCENE_WIDTH

        fun loadShader(type: Int, shaderCode: String): Int {
            return GLES31.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES31.glShaderSource(shader, shaderCode)
                GLES31.glCompileShader(shader)
            }
        }

        fun createAndLinkProgram(vertexShader: Int, fragmentShader: Int): Int {
            return GLES31.glCreateProgram().also {
                // add the vertex shader to program
                GLES31.glAttachShader(it, vertexShader)

                // add the fragment shader to program
                GLES31.glAttachShader(it, fragmentShader)

                // creates OpenGL ES program executables
                GLES31.glLinkProgram(it)
            }
        }


    }
}