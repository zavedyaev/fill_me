package ru.zavedyaev.fillme.primitive

class Circle2D(
    center: Point2D,
    radius: Float
) : Circle(center, Point(0f, 0f, 1f), radius) {

    val triangles = calcTriangles()

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

    fun draw(positionHandle: Int, colorHandle: Int, color: FloatArray) {
        triangles.forEach { it.draw(positionHandle, colorHandle, color) }
    }

    companion object {

        private val defaultCircleTriangles = calcTrianglesForDefaultCircle()
        /**
         * Calculates triangles for circle with center in 0,0 and radius 1
         * created for optimization
         */
        private fun calcTrianglesForDefaultCircle(): List<Triangle> {
            val center = Point2D(0f, 0f)
            val radius = 1
            val trianglesCount = 120

            val triangles = ArrayList<Triangle>()

            val angleDelta = 360.toDouble() / trianglesCount


            for (i in 0..(trianglesCount - 1)) {
                val a = center
                val b = Point2D(
                    center.x + radius * Math.cos(Math.toRadians(i * angleDelta)).toFloat(),
                    center.y + radius * Math.sin(Math.toRadians(i * angleDelta)).toFloat()
                )
                val c = Point2D(
                    center.x + radius * Math.cos(Math.toRadians(i * angleDelta + angleDelta)).toFloat(),
                    center.y + radius * Math.sin(Math.toRadians(i * angleDelta + angleDelta)).toFloat()
                )

                triangles.add(Triangle(a, b, c))
            }

            return triangles
        }
    }
}