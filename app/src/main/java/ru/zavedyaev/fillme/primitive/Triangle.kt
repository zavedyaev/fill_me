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

    private val vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(COORDS_COUNT * Point.BYTES_PER_VERTEX).run {
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

    fun draw(positionHandle: Int, mColorHandle: Int, color: FloatArray) {
        GLES31.glEnableVertexAttribArray(positionHandle)

        // Prepare the triangle coordinate data
        GLES31.glVertexAttribPointer(
            positionHandle,
            Point.COORDS_PER_VERTEX,
            GLES31.GL_FLOAT,
            false,
            VERTEX_STRIDE,
            vertexBuffer
        )

        // Set color for drawing the triangle
        GLES31.glUniform4fv(mColorHandle, 1, color, 0)

        // Draw the triangle
        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, Triangle.VERTEX_COUNT)

        // Disable vertex array
        GLES31.glDisableVertexAttribArray(positionHandle)
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
        const val VERTEX_COUNT = 3
        const val COORDS_COUNT = VERTEX_COUNT * Point.COORDS_PER_VERTEX
        const val VERTEX_STRIDE = Point.COORDS_PER_VERTEX * Point.BYTES_PER_VERTEX

    }
}