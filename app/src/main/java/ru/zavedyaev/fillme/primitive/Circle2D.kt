package ru.zavedyaev.fillme.primitive

import android.opengl.GLES31
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Circle2D(
    center: Point2D,
    radius: Float
) : Circle(center, Point(0f, 0f, 1f), radius) {

    private val triangles = calcTriangles()

    private fun calcTriangles(): List<Triangle> {
        return defaultCircleTriangles.map { triangle ->
            Triangle(
                center,
                Point2D(triangle.b.x * radius + center.x, triangle.b.y * radius + center.y),
                Point2D(triangle.c.x * radius + center.x, triangle.c.y * radius + center.y)
            )
        }
    }

    fun containsPoint(point: Point2D): Boolean {
        val line = Line(point, center)
        return line.getLength() <= radius
    }

    fun draw(positionHandle: Int, colorHandle: Int, textureHandle: Int, color: FloatArray) {
        draw(positionHandle, colorHandle, textureHandle, color, color)
    }

    fun draw(
        positionHandle: Int,
        colorHandle: Int,
        textureHandle: Int,
        centerColor: FloatArray,
        borderColor: FloatArray
    ) {
        // Set color for drawing the triangle
        val colorBuffer: FloatBuffer =
            ByteBuffer.allocateDirect(Triangle.COLOR_COORDS_COUNT * Point.BYTES_PER_FLOAT).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(centerColor + borderColor + borderColor)
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }

        triangles.forEachIndexed { index, triangle ->
            // Prepare the triangle coordinate data
            GLES31.glVertexAttribPointer(
                positionHandle,
                Point.COORDS_PER_POINT,
                GLES31.GL_FLOAT,
                false,
                Point.BYTES_PER_POINT,
                triangle.vertexBuffer
            )
            GLES31.glEnableVertexAttribArray(positionHandle)

            GLES31.glVertexAttribPointer(
                colorHandle,
                4, //color components per vertex
                GLES31.GL_FLOAT,
                false,
                Triangle.VERTEX_COLOR_STRIDE,
                colorBuffer
            )
            GLES31.glEnableVertexAttribArray(colorHandle)

            val texTriangle = circleTrianglesTextureCoordinates[index]

            // Set texture coords for drawing the triangle
            val texBuffer: FloatBuffer =
                ByteBuffer.allocateDirect(Triangle.POINT_COUNT * 2 * Point.BYTES_PER_FLOAT).run {
                    // use the device hardware's native byte order
                    order(ByteOrder.nativeOrder())

                    // create a floating point buffer from the ByteBuffer
                    asFloatBuffer().apply {
                        // add the coordinates to the FloatBuffer
                        put(
                            floatArrayOf(
                                texTriangle.a.x,
                                texTriangle.a.y,
                                texTriangle.b.x,
                                texTriangle.b.y,
                                texTriangle.c.x,
                                texTriangle.c.y
                            )
                        )
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


    }

    companion object {

        private val defaultCircleTriangles = calcTrianglesForDefaultCircle()
        private val circleTrianglesTextureCoordinates = calcTrianglesForCircleTextureCoordinates()
        /**
         * Calculates triangles for circle with center in 0,0 and radius 1
         * created for optimization
         */
        private fun calcTrianglesForDefaultCircle(): List<Triangle> {
            val center = Point2D(0f, 0f)
            val radius = 1f
            val trianglesCount = 120

            return calcTriangles(center, radius, trianglesCount)
        }

        private fun calcTrianglesForCircleTextureCoordinates(): List<Triangle> {
            val center = Point2D(0.5f, 0.5f)
            val radius = 0.5f
            val trianglesCount = 120

            return calcTriangles(center, radius, trianglesCount)
        }


        private fun calcTriangles(center: Point2D, radius: Float, trianglesCount: Int): List<Triangle> {
            val triangles = ArrayList<Triangle>()

            val angleDelta = 360.toDouble() / trianglesCount


            for (i in 0..(trianglesCount - 1)) {
                val b = Point2D(
                    center.x + radius * Math.cos(Math.toRadians(i * angleDelta)).toFloat(),
                    center.y + radius * Math.sin(Math.toRadians(i * angleDelta)).toFloat()
                )
                val c = Point2D(
                    center.x + radius * Math.cos(Math.toRadians(i * angleDelta + angleDelta)).toFloat(),
                    center.y + radius * Math.sin(Math.toRadians(i * angleDelta + angleDelta)).toFloat()
                )

                triangles.add(Triangle(center, b, c))
            }

            return triangles
        }
    }
}