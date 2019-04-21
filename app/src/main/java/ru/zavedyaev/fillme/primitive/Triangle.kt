package ru.zavedyaev.fillme.primitive

import android.opengl.GLES31
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Represents triangle
 *      a
 *     / \
 *    /   \
 *   b----c
 */
open class Triangle(
    val a: Point,
    val b: Point,
    val c: Point
) {
    private val ab = Line(a, b)
    private val ac = Line(a, c)
    private val bc = Line(b, c)

    val lines = listOf(ab, ac, bc)

    fun getSquare(): Float {
        val abLength = ab.getLength()
        val acLength = ac.getLength()
        val bcLength = bc.getLength()

        val perimeter = abLength + acLength + bcLength
        val perimeterHalf = perimeter / 2

        return Math.sqrt(
            (perimeterHalf *
                    (perimeterHalf - abLength) *
                    (perimeterHalf - acLength) *
                    (perimeterHalf - bcLength)).toDouble()

        ).toFloat()
    }

    val vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(COORDS_COUNT * Point.BYTES_PER_FLOAT).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(floatArrayOf(a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z))
                // set the buffer to read the first coordinate
                position(0)
            }
        }

    fun draw(positionHandle: Int, colorHandle: Int, textureHandle: Int, color: FloatArray) {
        draw(positionHandle, colorHandle, textureHandle, color, color, color)
    }
    
    fun draw(positionHandle: Int, colorHandle: Int, textureHandle: Int, colorA: FloatArray, colorB: FloatArray, colorC: FloatArray) {
        // Prepare the triangle coordinate data
        GLES31.glVertexAttribPointer(
            positionHandle,
            Point.COORDS_PER_POINT,
            GLES31.GL_FLOAT,
            false,
            Point.BYTES_PER_POINT,
            vertexBuffer
        )
        GLES31.glEnableVertexAttribArray(positionHandle)

        // Set color for drawing the triangle
        val colorBuffer: FloatBuffer =
            ByteBuffer.allocateDirect(COLOR_COORDS_COUNT * Point.BYTES_PER_FLOAT).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(colorA + colorB + colorC)
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }
        GLES31.glVertexAttribPointer(
            colorHandle,
            4, //color components per vertex
            GLES31.GL_FLOAT,
            false,
            VERTEX_COLOR_STRIDE,
            colorBuffer
        )
        GLES31.glEnableVertexAttribArray(colorHandle)


        
        // Set texture coords for drawing the triangle
        val texBuffer: FloatBuffer =
            ByteBuffer.allocateDirect(POINT_COUNT * 2 * Point.BYTES_PER_FLOAT).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(floatArrayOf((a.x+4.5f)/14, (a.y + 7)/14, (b.x+4.5f)/14, (b.y + 7)/14, (c.x+4.5f)/14, (c.y+7)/14))
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }
        GLES31.glVertexAttribPointer(
            textureHandle,
            2, //texture coords per vertex
            GLES31.GL_FLOAT,
            false,
            2 * Point.BYTES_PER_FLOAT,
            texBuffer
        )
        GLES31.glEnableVertexAttribArray(textureHandle)


        // Draw the triangle
        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, Triangle.POINT_COUNT)
    }

    /**
     * Applicable only for 2D triangles
     */
    fun containsPoint2D(point: Point2D): Boolean {
        val result1 = (a.x - point.x) * (c.y - a.y) - (c.x - a.x) * (a.y - point.y)
        val result2 = (c.x - point.x) * (b.y - c.y) - (b.x - c.x) * (c.y - point.y)
        val result3 = (b.x - point.x) * (a.y - b.y) - (a.x - b.x) * (b.y - point.y)

        return (result1 <= 0 && result2 <= 0 && result3 <= 0) || (result1 >= 0 && result2 >= 0 && result3 >= 0)
    }

    companion object {
        const val POINT_COUNT = 3
        const val COORDS_COUNT = POINT_COUNT * Point.COORDS_PER_POINT

        const val COLOR_SIZE_PER_VERTEX = 4
        const val COLOR_COORDS_COUNT = POINT_COUNT * COLOR_SIZE_PER_VERTEX
        const val VERTEX_COLOR_STRIDE = COLOR_SIZE_PER_VERTEX * Point.BYTES_PER_FLOAT

    }
}