package ru.zavedyaev.fillme.level

import ru.zavedyaev.fillme.primitive.Line
import ru.zavedyaev.fillme.primitive.Point2D
import ru.zavedyaev.fillme.primitive.Quadrilateral
import ru.zavedyaev.fillme.primitive.Triangle
import kotlin.math.roundToInt

object Border2D {
    const val thickness = 0.03f
    private const val halfThickness = thickness / 2
    private const val minSpaceLength = 0.3f
    private const val dotLength = 0.1f

    private val triangles: List<Triangle> =
        makeVerticalBorder(
            Point2D(-4.5f - halfThickness, 7f + halfThickness),
            Point2D(-4.5f - halfThickness, -7f + minSpaceLength + halfThickness),
            thickness,
            minSpaceLength
        ) +
                makeVerticalBorder(
                    Point2D(4.5f + halfThickness, -7f - halfThickness),
                    Point2D(4.5f + halfThickness, 7f - minSpaceLength - halfThickness),
                    thickness,
                    minSpaceLength
                ) +
                makeHorizontalBorder(
                    Point2D(-4.5f - halfThickness, -7f - halfThickness),
                    Point2D(4.5f - minSpaceLength - halfThickness, -7f - halfThickness),
                    thickness,
                    minSpaceLength
                ) +
                makeHorizontalBorder(
                    Point2D(4.5f + halfThickness, 7f + halfThickness),
                    Point2D(-4.5f + minSpaceLength + halfThickness, 7f + halfThickness),
                    thickness,
                    minSpaceLength
                )

    fun draw(positionHandle: Int, colorHandle: Int, color: FloatArray) {
        triangles.forEach { triangle ->
            triangle.draw(positionHandle, colorHandle, color)
        }
    }

    /**
     * Will bake a border with start right on start point, and end on end point, distance could be changed to fill whole line
     */
    private fun makeVerticalBorder(
        start: Point2D,
        end: Point2D,
        thickness: Float,
        minSpaceLength: Float
    ): List<Triangle> {
        val thicknessHalf = thickness / 2
        val triangles = ArrayList<Triangle>()

        val line = Line(start, end)

        val lineLength = line.getLength()

        val spacesCount = Math.floor((lineLength - dotLength).toDouble() / (minSpaceLength + dotLength)).roundToInt()
        val spaceLength = (lineLength - ((spacesCount + 1) * dotLength)) / spacesCount

        val moveUp = start.y < end.y
        val dotWithSpaceLength = dotLength + spaceLength

        for (i in 1..spacesCount) {
            if (moveUp) {
                triangles.addAll(
                    Quadrilateral(
                        Point2D(start.x - thicknessHalf, start.y + (i - 1) * dotWithSpaceLength),
                        Point2D(start.x + thicknessHalf, start.y + (i - 1) * dotWithSpaceLength),
                        Point2D(start.x + thicknessHalf, start.y + (i - 1) * dotWithSpaceLength + dotLength),
                        Point2D(start.x - thicknessHalf, start.y + (i - 1) * dotWithSpaceLength + dotLength)
                    ).getTriangles()
                )
            } else {
                triangles.addAll(
                    Quadrilateral(
                        Point2D(start.x - thicknessHalf, start.y - (i - 1) * dotWithSpaceLength),
                        Point2D(start.x - thicknessHalf, start.y - (i - 1) * dotWithSpaceLength - dotLength),
                        Point2D(start.x + thicknessHalf, start.y - (i - 1) * dotWithSpaceLength - dotLength),
                        Point2D(start.x + thicknessHalf, start.y - (i - 1) * dotWithSpaceLength)
                    ).getTriangles()
                )
            }
        }


        if (moveUp) {
            triangles.addAll(
                Quadrilateral(
                    Point2D(start.x - thicknessHalf, end.y),
                    Point2D(start.x - thicknessHalf, end.y - dotLength),
                    Point2D(start.x + thicknessHalf, end.y - dotLength),
                    Point2D(start.x + thicknessHalf, end.y)
                ).getTriangles()
            )
        } else {
            triangles.addAll(
                Quadrilateral(
                    Point2D(start.x - thicknessHalf, end.y),
                    Point2D(start.x + thicknessHalf, end.y),
                    Point2D(start.x + thicknessHalf, end.y + dotLength),
                    Point2D(start.x - thicknessHalf, end.y + dotLength)
                ).getTriangles()
            )
        }

        return triangles
    }

    /**
     * Will bake a border with start right on start point, and end on end point, distance could be changed to fill whole line
     */
    private fun makeHorizontalBorder(
        start: Point2D,
        end: Point2D,
        thickness: Float,
        minSpaceLength: Float
    ): List<Triangle> {
        val thicknessHalf = thickness / 2
        val triangles = ArrayList<Triangle>()

        val line = Line(start, end)

        val lineLength = line.getLength()

        val spacesCount = Math.floor((lineLength - dotLength).toDouble() / (minSpaceLength + dotLength)).roundToInt()
        val spaceLength = (lineLength - ((spacesCount + 1) * dotLength)) / spacesCount

        val moveRight = start.x < end.x
        val dotWithSpaceLength = dotLength + spaceLength

        for (i in 1..spacesCount) {
            if (moveRight) {
                triangles.addAll(
                    Quadrilateral(
                        Point2D(start.x + (i - 1) * dotWithSpaceLength, start.y + thicknessHalf),
                        Point2D(start.x + (i - 1) * dotWithSpaceLength, start.y - thicknessHalf),
                        Point2D(start.x + (i - 1) * dotWithSpaceLength + dotLength, start.y - thicknessHalf),
                        Point2D(start.x + (i - 1) * dotWithSpaceLength + dotLength, start.y + thicknessHalf)
                    ).getTriangles()
                )
            } else {
                triangles.addAll(
                    Quadrilateral(
                        Point2D(start.x - (i - 1) * dotWithSpaceLength, start.y - thicknessHalf),
                        Point2D(start.x - (i - 1) * dotWithSpaceLength, start.y + thicknessHalf),
                        Point2D(start.x - (i - 1) * dotWithSpaceLength - dotLength, start.y + thicknessHalf),
                        Point2D(start.x - (i - 1) * dotWithSpaceLength - dotLength, start.y - thicknessHalf)

                    ).getTriangles()
                )
            }
        }


        if (moveRight) {
            triangles.addAll(
                Quadrilateral(
                    Point2D(end.x, start.y - thicknessHalf),
                    Point2D(end.x, start.y + thicknessHalf),
                    Point2D(end.x - dotLength, start.y + thicknessHalf),
                    Point2D(end.x - dotLength, start.y - thicknessHalf)
                ).getTriangles()
            )
        } else {
            triangles.addAll(
                Quadrilateral(
                    Point2D(end.x, start.y + thicknessHalf),
                    Point2D(end.x, start.y - thicknessHalf),
                    Point2D(end.x + dotLength, start.y - thicknessHalf),
                    Point2D(end.x + dotLength, start.y + thicknessHalf)
                ).getTriangles()
            )
        }

        return triangles
    }
}