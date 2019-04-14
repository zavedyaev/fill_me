package ru.zavedyaev.fillme.level

import ru.zavedyaev.fillme.primitive.Point2D
import ru.zavedyaev.fillme.primitive.Quadrilateral
import ru.zavedyaev.fillme.primitive.Triangle

object LevelsPacks {
    val packs = mapOf(0 to levelPack0)
}

val levelPack0 = LevelPack(
    mapOf(
        0 to GameLevel(
            getTrianglesAroundCircle(3f, Point2D(0f, 0f))
                    + Quadrilateral(
                Point2D(-4.5f, 3f),
                Point2D(4.5f, 3f),
                Point2D(4.5f, 7f),
                Point2D(-4.5f, 7f)
            ).getTriangles()
                    + Quadrilateral(
                Point2D(-4.5f, -3f),
                Point2D(-4.5f, -7f),
                Point2D(4.5f, -7f),
                Point2D(4.5f, -3f)
            ).getTriangles()
                    + Quadrilateral(
                Point2D(-4.5f, 3f),
                Point2D(-4.5f, -3f),
                Point2D(-3f, -3f),
                Point2D(-3f, 3f)
            ).getTriangles()
                    + Quadrilateral(
                Point2D(3f, 3f),
                Point2D(3f, -3f),
                Point2D(4.5f, -3f),
                Point2D(4.5f, 3f)
            ).getTriangles()
                    + GameLevel.fillAll(),

            WinConditions(
                WinCondition(0.4f, 2),
                WinCondition(0.55f, 1),
                WinCondition(0.65f, 1)
            ),
            0
        ),
        1 to GameLevel(
            getTrianglesAroundCircle(3f, Point2D(-1.5f, 4f)) + getTrianglesAroundCircle(3f, Point2D(1.5f, -4f)) +
                    Quadrilateral(
                        Point2D(-4.5f, 1f),
                        Point2D(-4.5f, -7f),
                        Point2D(-1.5f, -7f),
                        Point2D(-1.5f, 1f)
                    ).getTriangles() +
                    Quadrilateral(
                        Point2D(-1.5f, 1f),
                        Point2D(-1.5f, -1f),
                        Point2D(1.5f, -1f),
                        Point2D(1.5f, 1f)
                    ).getTriangles()
                    + Quadrilateral(
                Point2D(1.5f, 7f),
                Point2D(1.5f, -1f),
                Point2D(4.5f, -1f),
                Point2D(4.5f, 7f)
            ).getTriangles() +
                    GameLevel.fillAll(),

            WinConditions(
                WinCondition(0.4f, 4),
                WinCondition(0.55f, 3),
                WinCondition(0.65f, 2)
            ),
            0
        ),
        2 to GameLevel(
            listOf(
                Triangle(
                    Point2D(0f, 5f),
                    Point2D(-3f, 5f),
                    Point2D(-3f, -5f)
                ), Triangle(
                    Point2D(0f, 5f),
                    Point2D(3f, -5f),
                    Point2D(3f, 5f)
                )
            ) +
                    Quadrilateral(
                        Point2D(-4.5f, 5f),
                        Point2D(4.5f, 5f),
                        Point2D(4.5f, 7f),
                        Point2D(-4.5f, 7f)
                    ).getTriangles() +
                    Quadrilateral(
                        Point2D(-4.5f, -5f),
                        Point2D(-4.5f, -7f),
                        Point2D(4.5f, -7f),
                        Point2D(4.5f, -5f)
                    ).getTriangles() +

                    Quadrilateral(
                        Point2D(-4.5f, 5f),
                        Point2D(-4.5f, -5f),
                        Point2D(-3f, -5f),
                        Point2D(-3f, 5f)
                    ).getTriangles() +
                    Quadrilateral(
                        Point2D(3f, 5f),
                        Point2D(3f, -5f),
                        Point2D(4.5f, -5f),
                        Point2D(4.5f, 5f)
                    ).getTriangles() +


                    GameLevel.fillAll(),

            WinConditions(
                WinCondition(0.65f, 6),
                WinCondition(0.65f, 5),
                WinCondition(0.65f, 4)
            ),
            1
        ),
        3 to GameLevel(
            emptyList()
            ,

            WinConditions(
                WinCondition(0.999f, 3),
                WinCondition(0.999f, 2),
                WinCondition(0.999f, 1)
            ),
            3
        ),
        4 to GameLevel(
            getTrianglesAroundCircle(14f, Point2D(0f, -7f)) + GameLevel.fillTop()
            ,

            WinConditions(
                WinCondition(0.90f, 3),
                WinCondition(0.90f, 2),
                WinCondition(0.90f, 1)
            ),
            4
        ),
        5 to GameLevel(
            getTrianglesAroundCircle(6f, Point2D(0f, -6f)) + getTrianglesAroundCircle(6f, Point2D(0f, 6f))
            ,

            WinConditions(
                WinCondition(0.60f, 2),
                WinCondition(0.75f, 2),
                WinCondition(0.90f, 2)
            ),
            6
        ),
        6 to GameLevel(
            listOf(
                Triangle(Point2D(-4.5f, 0f), Point2D(-4.5f, -4.5f), Point2D(0f, -4.5f)),
                Triangle(Point2D(0f, -4.5f), Point2D(4.5f, -4.5f), Point2D(4.5f, 0f)),
                Triangle(Point2D(0f, 4.5f), Point2D(4.5f, 0f), Point2D(4.5f, 4.5f)),
                Triangle(Point2D(-4.5f, 0f), Point2D(0f, 4.5f), Point2D(-4.5f, 4.5f))
            ) + Quadrilateral(
                Point2D(-4.5f, -4.5f),
                Point2D(-4.5f, -7f),
                Point2D(4.5f, -7f),
                Point2D(4.5f, -4.5f)
            ).getTriangles()
                    + Quadrilateral(
                Point2D(-4.5f, 4.5f),
                Point2D(-4.5f, 7f),
                Point2D(4.5f, 7f),
                Point2D(4.5f, 4.5f)
            ).getTriangles()
                    + GameLevel.fillAll(),

            SameMaxCirclesCountWinCondition(0.5f, 0.6f, 0.7f, 10),
            8
        )
    )
)

private fun getTrianglesAroundCircle(radius: Float, center: Point2D): List<Triangle> {
    val fortyFive = radius * 0.707f

    return listOf(
        Triangle(
            Point2D(-radius + center.x, radius + center.y),
            Point2D(-radius + center.x, 0f + center.y),
            Point2D(-fortyFive + center.x, fortyFive + center.y)
        ),
        Triangle(
            Point2D(-radius + center.x, radius + center.y),
            Point2D(-fortyFive + center.x, fortyFive + center.y),
            Point2D(0f + center.x, radius + center.y)
        ),

        Triangle(
            Point2D(-radius + center.x, 0f + center.y),
            Point2D(-radius + center.x, -radius + center.y),
            Point2D(-fortyFive + center.x, -fortyFive + center.y)
        ),
        Triangle(
            Point2D(-radius + center.x, -radius + center.y),
            Point2D(0f + center.x, -radius + center.y),
            Point2D(-fortyFive + center.x, -fortyFive + center.y)
        ),

        Triangle(
            Point2D(0f + center.x, -radius + center.y),
            Point2D(radius + center.x, -radius + center.y),
            Point2D(fortyFive + center.x, -fortyFive + center.y)
        ),
        Triangle(
            Point2D(fortyFive + center.x, -fortyFive + center.y),
            Point2D(radius + center.x, -radius + center.y),
            Point2D(radius + center.x, 0f + center.y)
        ),


        Triangle(
            Point2D(0f + center.x, radius + center.y),
            Point2D(fortyFive + center.x, fortyFive + center.y),
            Point2D(radius + center.x, radius + center.y)
        ),
        Triangle(
            Point2D(fortyFive + center.x, fortyFive + center.y),
            Point2D(radius + center.x, 0f + center.y),
            Point2D(radius + center.x, radius + center.y)
        )
    )
}

data class LevelPack(
    val levels: Map<Int, GameLevel>
)