package ru.zavedyaev.fillme.level

import ru.zavedyaev.fillme.primitive.Point2D
import ru.zavedyaev.fillme.primitive.Quadrilateral
import ru.zavedyaev.fillme.primitive.Triangle

class GameLevel(
    val triangles: List<Triangle>,
    val winConditions: WinConditions,
    val starsToUnlock: Int
) {
    fun draw(positionHandle: Int, colorHandle: Int, color: FloatArray) {
        triangles.forEach { triangle ->
            triangle.draw(positionHandle, colorHandle, color)
        }
    }

    fun containsPoint(point: Point2D): Boolean {
        triangles.forEach { triangle ->
            if (triangle.containsPoint2D(point)) return true
        }
        return false
    }

    companion object {
        fun fillTop(): List<Triangle> {
            return Quadrilateral(
                Point2D(-4.5f, 7f), Point2D(4.5f, 7f), Point2D(4.5f, 21f), Point2D(-4.5f, 21f)
            ).getTriangles()
        }

        fun fillBottom(): List<Triangle> {
            return Quadrilateral(
                Point2D(-4.5f, -7f), Point2D(-4.5f, -21f), Point2D(4.5f, -21f), Point2D(4.5f, -7f)
            ).getTriangles()
        }

        fun fillLeft(): List<Triangle> {
            return Quadrilateral(
                Point2D(-4.5f, -7f), Point2D(-4.5f, 7f), Point2D(-13.5f, 7f), Point2D(-13.5f, -7f)
            ).getTriangles()
        }

        fun fillRight(): List<Triangle> {
            return Quadrilateral(
                Point2D(4.5f, 7f), Point2D(4.5f, -7f), Point2D(13.5f, -7f), Point2D(13.5f, 7f)
            ).getTriangles()
        }

        fun fillTopLeft(): List<Triangle> {
            return Quadrilateral(
                Point2D(-4.5f, 7f), Point2D(-4.5f, 21f), Point2D(-13.5f, 21f), Point2D(-13.5f, 7f)
            ).getTriangles()
        }

        fun fillTopRight(): List<Triangle> {
            return Quadrilateral(
                Point2D(4.5f, 7f), Point2D(13.5f, 7f), Point2D(13.5f, 21f), Point2D(4.5f, 21f)
            ).getTriangles()
        }

        fun fillBottomLeft(): List<Triangle> {
            return Quadrilateral(
                Point2D(-4.5f, -7f), Point2D(-13.5f, -7f), Point2D(-13.5f, -21f), Point2D(-4.5f, -21f)
            ).getTriangles()
        }

        fun fillBottomRight(): List<Triangle> {
            return Quadrilateral(
                Point2D(4.5f, -7f), Point2D(4.5f, -21f), Point2D(13.5f, -21f), Point2D(13.5f, -7f)
            ).getTriangles()
        }

        fun fillAll(): List<Triangle> {
            return fillTop() + fillBottom() + fillLeft() + fillRight() + fillTopLeft() + fillTopRight() + fillBottomLeft() + fillBottomRight()
        }
    }
}

class SameMaxCirclesCountWinCondition(
    oneStarMinSquare: Float,
    twoStarsMinSquare: Float,
    threeStarsMinSquare: Float,
    maxCirclesCount: Int
) : WinConditions(
    WinCondition(oneStarMinSquare, maxCirclesCount),
    WinCondition(twoStarsMinSquare, maxCirclesCount),
    WinCondition(threeStarsMinSquare, maxCirclesCount)
)

class SameMinSquareWinCondition(
    minSquare: Float,
    oneStarCirclesCount: Int,
    twoStarsCirclesCount: Int,
    threeStarsCirclesCount: Int
) : WinConditions(
    WinCondition(minSquare, oneStarCirclesCount),
    WinCondition(minSquare, twoStarsCirclesCount),
    WinCondition(minSquare, threeStarsCirclesCount)
)

open class WinConditions(
    val oneStar: WinCondition,
    val twoStars: WinCondition,
    val threeStars: WinCondition
)

data class WinCondition(
    val minSquare: Float,
    val maxCirclesCount: Int
)