package ru.zavedyaev.fillme.shader

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES31
import android.opengl.GLUtils
import ru.zavedyaev.fillme.R
import java.util.*


object TextureHelper {
    fun loadAllCircleTextures(context: Context): List<Int> {
        return circleTextures.map { loadTexture(context, it) }
    }

    fun loadTexture(context: Context, resourceId: Int): Int {
        val textureHandle = IntArray(1)

        GLES31.glGenTextures(1, textureHandle, 0)

        if (textureHandle[0] == 0) {
            throw RuntimeException("Error generating texture name.")
        }

        val options = BitmapFactory.Options()
        options.inScaled = false    // No pre-scaling

        // Read in the resource
        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)

        // Bind to the texture in OpenGL
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureHandle[0])

        // Set filtering
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST)
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_NEAREST)

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, bitmap, 0)

        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle()

        return textureHandle[0]
    }

    private val circleTextures = listOf(
        R.drawable.texture_circle_1,
        R.drawable.texture_circle_2,
        R.drawable.texture_circle_3,
        R.drawable.texture_circle_4,
        R.drawable.texture_circle_5,
        R.drawable.texture_circle_6,
        R.drawable.texture_circle_7,
        R.drawable.texture_circle_8,
        R.drawable.texture_circle_9,
        R.drawable.texture_circle_10,
        R.drawable.texture_circle_11,
        R.drawable.texture_circle_12,
        R.drawable.texture_circle_13,
        R.drawable.texture_circle_14,
        R.drawable.texture_circle_15,
        R.drawable.texture_circle_16,
        R.drawable.texture_circle_17,
        R.drawable.texture_circle_18,
        R.drawable.texture_circle_19,
        R.drawable.texture_circle_20,
        R.drawable.texture_circle_21,
        R.drawable.texture_circle_22,
        R.drawable.texture_circle_23,
        R.drawable.texture_circle_24,
        R.drawable.texture_circle_25,
        R.drawable.texture_circle_26,
        R.drawable.texture_circle_27,
        R.drawable.texture_circle_28,
        R.drawable.texture_circle_29
    )

    private val random = Random()
    fun getRandomCircleTextureId() = random.nextInt(circleTextures.size)
}