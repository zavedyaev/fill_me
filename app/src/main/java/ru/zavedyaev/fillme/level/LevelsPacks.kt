package ru.zavedyaev.fillme.level

import ru.zavedyaev.fillme.primitive.Point2D
import ru.zavedyaev.fillme.primitive.Quadrilateral
import ru.zavedyaev.fillme.primitive.Triangle

object LevelsPacks {
    val packs = mapOf(0 to levelPack0)
}

val levelPack0 = LevelPack(
    mapOf(
        0 to GameLevel( //circle
            getTrianglesAroundCircle(3f, Point2D(0f, 0f)) + Quadrilateral(
                Point2D(-4.5f, 3f),
                Point2D(4.5f, 3f),
                Point2D(4.5f, 7f),
                Point2D(-4.5f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(-4.5f, -3f),
                Point2D(-4.5f, -7f),
                Point2D(4.5f, -7f),
                Point2D(4.5f, -3f)
            ).getTriangles() + Quadrilateral(
                Point2D(-4.5f, 3f),
                Point2D(-4.5f, -3f),
                Point2D(-3f, -3f),
                Point2D(-3f, 3f)
            ).getTriangles() + Quadrilateral(
                Point2D(3f, 3f),
                Point2D(3f, -3f),
                Point2D(4.5f, -3f),
                Point2D(4.5f, 3f)
            ).getTriangles() + GameLevel.fillAll(),

            WinConditions(
                WinCondition(0.4f, 2),
                WinCondition(0.55f, 1),
                WinCondition(0.65f, 1)
            ),
            0
        ),
        1 to GameLevel( // 2 circles
            getTrianglesAroundCircle(3f, Point2D(-1.5f, 4f)) +
                    getTrianglesAroundCircle(3f, Point2D(1.5f, -4f)) + Quadrilateral(
                Point2D(-4.5f, 1f),
                Point2D(-4.5f, -7f),
                Point2D(-1.5f, -7f),
                Point2D(-1.5f, 1f)
            ).getTriangles() + Quadrilateral(
                Point2D(-1.5f, 1f),
                Point2D(-1.5f, -1f),
                Point2D(1.5f, -1f),
                Point2D(1.5f, 1f)
            ).getTriangles() + Quadrilateral(
                Point2D(1.5f, 7f),
                Point2D(1.5f, -1f),
                Point2D(4.5f, -1f),
                Point2D(4.5f, 7f)
            ).getTriangles() + GameLevel.fillAll(),

            WinConditions(
                WinCondition(0.4f, 4),
                WinCondition(0.55f, 3),
                WinCondition(0.65f, 2)
            ),
            1
        ),

        2 to GameLevel( //3 circles
            getTrianglesAroundCircle(3f, Point2D(0f, 3f)) +
                    getTrianglesAroundCircle(3f, Point2D(-3.5f, -3f)) +
                    getTrianglesAroundCircle(3f, Point2D(3.5f, -3f)) + Quadrilateral(
                Point2D(-0.5f, 0f),
                Point2D(-0.5f, -6f),
                Point2D(0.5f, -6f),
                Point2D(0.5f, 0f)
            ).getTriangles() +

                    Quadrilateral(
                        Point2D(-6.5f, -6f),
                        Point2D(-6.5f, -7f),
                        Point2D(6.5f, -7f),
                        Point2D(6.5f, -6f)
                    ).getTriangles() +

                    Quadrilateral(
                        Point2D(-3f, 7f),
                        Point2D(-3f, 6f),
                        Point2D(3f, 6f),
                        Point2D(3f, 7f)
                    ).getTriangles() +

                    Quadrilateral(
                        Point2D(-13.5f, 7f),
                        Point2D(-13.5f, 0f),
                        Point2D(-3f, 0f),
                        Point2D(-3f, 7f)
                    ).getTriangles() +

                    Quadrilateral(
                        Point2D(3f, 7f),
                        Point2D(3f, 0f),
                        Point2D(13.5f, 0f),
                        Point2D(13.5f, 7f)
                    ).getTriangles() +

                    Quadrilateral(
                        Point2D(-13.5f, 0f),
                        Point2D(-13.5f, -7f),
                        Point2D(-6.5f, -7f),
                        Point2D(-6.5f, 0f)
                    ).getTriangles() +

                    Quadrilateral(
                        Point2D(6.5f, 0f),
                        Point2D(6.5f, -7f),
                        Point2D(13.5f, -7f),
                        Point2D(13.5f, 0f)
                    ).getTriangles() +

                    GameLevel.fillTop() + GameLevel.fillTopLeft() + GameLevel.fillTopRight() +
                    GameLevel.fillBottom() + GameLevel.fillBottomLeft() + GameLevel.fillBottomRight(),

            SameMaxCirclesCountWinCondition(0.55f, 0.65f, 0.75f, 3),
            3
        ),


        3 to GameLevel( //ellipse
            listOf(
                Triangle(Point2D(-4.5f, 7f), Point2D(-4.5f, 0f), Point2D(-3f, 6f)),
                Triangle(Point2D(-4.5f, 7f), Point2D(-3f, 6f), Point2D(0f, 7f)),

                Triangle(Point2D(0f, 7f), Point2D(3f, 6f), Point2D(4.5f, 7f)),
                Triangle(Point2D(3f, 6f), Point2D(4.5f, 0f), Point2D(4.5f, 7f)),

                Triangle(Point2D(-4.5f, 0f), Point2D(-4.5f, -7f), Point2D(-3f, -6f)),
                Triangle(Point2D(-3f, -6f), Point2D(-4.5f, -7f), Point2D(0f, -7f)),

                Triangle(Point2D(0f, -7f), Point2D(4.5f, -7f), Point2D(3f, -6f)),
                Triangle(Point2D(3f, -6f), Point2D(4.5f, -7f), Point2D(4.5f, 0f))
            ) + GameLevel.fillAll(),

            SameMaxCirclesCountWinCondition(0.5f, 0.65f, 0.75f, 3),
            4
        ),

        4 to GameLevel( //empty
            emptyList(),
            WinConditions(
                WinCondition(0.999f, 3),
                WinCondition(0.999f, 2),
                WinCondition(0.999f, 1)
            ),
            6
        ),

        5 to GameLevel( //2 marlboro
            listOf(
                Triangle(Point2D(-4.5f, 1.5f), Point2D(-4.5f, -1.5f), Point2D(0f, 0f)),
                Triangle(Point2D(0f, 0f), Point2D(4.5f, -1.5f), Point2D(4.5f, 1.5f))
            ) + Quadrilateral(
                Point2D(-13.5f, 1.5f),
                Point2D(-13.5f, -1.5f),
                Point2D(-4.5f, -1.5f),
                Point2D(-4.5f, 1.5f)
            ).getTriangles() + Quadrilateral(
                Point2D(4.5f, 1.5f),
                Point2D(4.5f, -1.5f),
                Point2D(13.5f, -1.5f),
                Point2D(13.5f, 1.5f)
            ).getTriangles(),

            WinConditions(
                WinCondition(0.60f, 2),
                WinCondition(0.75f, 2),
                WinCondition(0.90f, 2)
            ),
            9
        ),

        6 to GameLevel( //marlboro
            listOf(
                Triangle(Point2D(-4.5f, 7f), Point2D(-4.5f, 5.5f), Point2D(0f, 7f)),
                Triangle(Point2D(0f, 7f), Point2D(4.5f, 5.5f), Point2D(4.5f, 7f))
            ) + Quadrilateral(
                Point2D(-13.5f, 7f),
                Point2D(-13.5f, 5.5f),
                Point2D(-4.5f, 5.5f),
                Point2D(-4.5f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(4.5f, 7f),
                Point2D(4.5f, 5.5f),
                Point2D(13.5f, 5.5f),
                Point2D(13.5f, 7f)
            ).getTriangles() + GameLevel.fillTop() + GameLevel.fillTopRight() + GameLevel.fillTopLeft(),

            WinConditions(
                WinCondition(0.90f, 3),
                WinCondition(0.90f, 2),
                WinCondition(0.90f, 1)
            ),
            10
        ),

        7 to GameLevel( //4 circles
            listOf(
                Triangle(Point2D(-4.5f, 2.5f), Point2D(-1f, 2.5f), Point2D(-1f, 3.5f)),
                Triangle(Point2D(-1f, 3.5f), Point2D(1f, 3.5f), Point2D(0f, 7f)),
                Triangle(Point2D(1f, 3.5f), Point2D(1f, 2.5f), Point2D(4.5f, 2.5f)),

                Triangle(Point2D(-4.5f, -2.5f), Point2D(-1f, -3.5f), Point2D(-1f, -2.5f)),
                Triangle(Point2D(-1f, -3.5f), Point2D(0f, -7f), Point2D(1f, -3.5f)),
                Triangle(Point2D(1f, -2.5f), Point2D(1f, -3.5f), Point2D(4.5f, -2.5f))
            ) + Quadrilateral(
                Point2D(-1f, 3.5f),
                Point2D(-1f, 2.5f),
                Point2D(1f, 2.5f),
                Point2D(1f, 3.5f)
            ).getTriangles() + Quadrilateral(
                Point2D(-1f, -2.5f),
                Point2D(-1f, -3.5f),
                Point2D(1f, -3.5f),
                Point2D(1f, -2.5f)
            ).getTriangles() + Quadrilateral(
                Point2D(-13.5f, 2.5f),
                Point2D(-13.5f, -2.5f),
                Point2D(13.5f, -2.5f),
                Point2D(13.5f, 2.5f)
            ).getTriangles(),

            SameMaxCirclesCountWinCondition(0.5f, 0.6f, 0.7f, 4),
            12
        ),

        8 to GameLevel( //sad smile
            listOf(
                Triangle(Point2D(-4.5f, 4.5f), Point2D(-2.5f, 4.5f), Point2D(-2.5f, 5f)),
                Triangle(Point2D(-2f, 7f), Point2D(-2.5f, 5f), Point2D(-2f, 5f)),

                Triangle(Point2D(-4.5f, -4.5f), Point2D(-2.5f, -5f), Point2D(-2.5f, -4.5f)),
                Triangle(Point2D(-2.5f, -5f), Point2D(-2f, -7f), Point2D(-2f, -5f)),

                Triangle(Point2D(-0.5f, 5f), Point2D(-0.5f, 0f), Point2D(0.5f, 4f)),
                Triangle(Point2D(-0.5f, 5f), Point2D(0.5f, 4f), Point2D(4.5f, 5f)),

                Triangle(Point2D(-0.5f, 0f), Point2D(-0.5f, -5f), Point2D(0.5f, -4f)),
                Triangle(Point2D(-0.5f, -5f), Point2D(4.5f, -5f), Point2D(0.5f, -4f))
            ) + Quadrilateral(
                Point2D(-2.5f, 5f),
                Point2D(-2.5f, -5f),
                Point2D(-0.5f, -5f),
                Point2D(-0.5f, 5f)
            ).getTriangles() + Quadrilateral(
                Point2D(-13.5f, 4.5f),
                Point2D(-13.5f, -4.5f),
                Point2D(-2.5f, -4.5f),
                Point2D(-2.5f, 4.5f)
            ).getTriangles() + Quadrilateral(
                Point2D(-2f, 21f),
                Point2D(-2f, 5f),
                Point2D(13.5f, 5f),
                Point2D(13.5f, 21f)
            ).getTriangles() + Quadrilateral(
                Point2D(-2f, -5f),
                Point2D(-2f, -21f),
                Point2D(13.5f, -21f),
                Point2D(13.5f, -5f)
            ).getTriangles(),

            SameMaxCirclesCountWinCondition(0.5f, 0.6f, 0.7f, 3),
            14
        ),

        9 to GameLevel( //smile
            getTrianglesAroundCircle(1.5f, Point2D(-2f, 3f)) +
                    getTrianglesAroundCircle(1.5f, Point2D(2f, 3f)) +

                    listOf(
                        Triangle(Point2D(-1.5f, -1f), Point2D(0f, -2f), Point2D(1.5f, -1f)),

                        Triangle(Point2D(-4f, -1f), Point2D(-4f, -5f), Point2D(-3f, -4f)),
                        Triangle(Point2D(-3f, -4f), Point2D(-4f, -5f), Point2D(0f, -5f)),

                        Triangle(Point2D(0f, -5f), Point2D(4f, -5f), Point2D(3f, -4f)),
                        Triangle(Point2D(3f, -4f), Point2D(4f, -5f), Point2D(4f, -1f))
                    ) + Quadrilateral(
                Point2D(-3.5f, 7f),
                Point2D(-3.5f, 4.5f),
                Point2D(3.5f, 4.5f),
                Point2D(3.5f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(-3.5f, 1.5f),
                Point2D(-3.5f, -1f),
                Point2D(3.5f, -1f),
                Point2D(3.5f, 1.5f)
            ).getTriangles() + Quadrilateral(
                Point2D(-0.5f, 4.5f),
                Point2D(-0.5f, 1.5f),
                Point2D(0.5f, 1.5f),
                Point2D(0.5f, 4.5f)
            ).getTriangles() +

                    Quadrilateral(
                        Point2D(-4.5f, 7f),
                        Point2D(-4.5f, -1f),
                        Point2D(-3.5f, -1f),
                        Point2D(-3.5f, 7f)
                    ).getTriangles() + Quadrilateral(
                Point2D(3.5f, 7f),
                Point2D(3.5f, -1f),
                Point2D(4.5f, -1f),
                Point2D(4.5f, 7f)
            ).getTriangles() +

                    Quadrilateral(
                        Point2D(-4.5f, -1f),
                        Point2D(-4.5f, -7f),
                        Point2D(-4f, -7f),
                        Point2D(-4f, -1f)
                    ).getTriangles() + Quadrilateral(
                Point2D(4f, -1f),
                Point2D(4f, -7f),
                Point2D(4.5f, -7f),
                Point2D(4.5f, -1f)
            ).getTriangles() +

                    Quadrilateral(
                        Point2D(-4f, -5f),
                        Point2D(-4f, -7f),
                        Point2D(4f, -7f),
                        Point2D(4f, -5f)
                    ).getTriangles() +

                    GameLevel.fillAll(),

            SameMaxCirclesCountWinCondition(0.5f, 0.6f, 0.7f, 7),
            15
        ),

        10 to GameLevel( //burger
            listOf(
                Triangle(Point2D(-4f, 5f), Point2D(-4f, 1f), Point2D(-3f, 4f)),
                Triangle(Point2D(-4f, 5f), Point2D(-3f, 4f), Point2D(0f, 5f)),

                Triangle(Point2D(0f, 5f), Point2D(3f, 4f), Point2D(4f, 5f)),
                Triangle(Point2D(3f, 4f), Point2D(4f, 1f), Point2D(4f, 5f)),

                Triangle(Point2D(-4f, -1f), Point2D(-4f, -5f), Point2D(-3f, -4f)),
                Triangle(Point2D(-3f, -4f), Point2D(-4f, -5f), Point2D(0f, -5f)),

                Triangle(Point2D(0f, -5f), Point2D(4f, -5f), Point2D(3f, -4f)),
                Triangle(Point2D(3f, -4f), Point2D(4f, -5f), Point2D(4f, -1f))
            ) + Quadrilateral(
                Point2D(-4.5f, 5f),
                Point2D(-4.5f, 1f),
                Point2D(-4f, 1f),
                Point2D(-4f, 5f)
            ).getTriangles() + Quadrilateral(
                Point2D(4f, 5f),
                Point2D(4f, 1f),
                Point2D(4.5f, 1f),
                Point2D(4.5f, 5f)
            ).getTriangles() +

                    Quadrilateral(
                        Point2D(-4.5f, -1f),
                        Point2D(-4.5f, -5f),
                        Point2D(-4f, -5f),
                        Point2D(-4f, -1f)
                    ).getTriangles() + Quadrilateral(
                Point2D(4f, -1f),
                Point2D(4f, -5f),
                Point2D(4.5f, -5f),
                Point2D(4.5f, -1f)
            ).getTriangles() +

                    Quadrilateral(
                        Point2D(-4.5f, 1f),
                        Point2D(-4.5f, -1f),
                        Point2D(4.5f, -1f),
                        Point2D(4.5f, 1f)
                    ).getTriangles() +

                    Quadrilateral(
                        Point2D(-4.5f, 7f),
                        Point2D(-4.5f, 5f),
                        Point2D(4.5f, 5f),
                        Point2D(4.5f, 7f)
                    ).getTriangles() + Quadrilateral(
                Point2D(-4.5f, -5f),
                Point2D(-4.5f, -7f),
                Point2D(4.5f, -7f),
                Point2D(4.5f, -5f)
            ).getTriangles() +
                    GameLevel.fillAll(),

            SameMaxCirclesCountWinCondition(0.4f, 0.5f, 0.6f, 6),
            17
        ),

        11 to GameLevel( //triangle
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
            ) + Quadrilateral(
                Point2D(-4.5f, 5f),
                Point2D(4.5f, 5f),
                Point2D(4.5f, 7f),
                Point2D(-4.5f, 7f)
            ).getTriangles() + Quadrilateral(
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
                    ).getTriangles() + Quadrilateral(
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
            19
        ),

        12 to GameLevel( //pregnant
            Quadrilateral(
                Point2D(-13.5f, 7f),
                Point2D(-13.5f, 5f),
                Point2D(13.5f, 5f),
                Point2D(13.5f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(-13.5f, 1f),
                Point2D(-13.5f, -1f),
                Point2D(13.5f, -1f),
                Point2D(13.5f, 1f)
            ).getTriangles() +

                    Quadrilateral(
                        Point2D(-13.5f, -5f),
                        Point2D(-13.5f, -7f),
                        Point2D(13.5f, -7f),
                        Point2D(13.5f, -5f)
                    ).getTriangles() +

                    GameLevel.fillTop() + GameLevel.fillTopLeft() + GameLevel.fillTopRight() +
                    GameLevel.fillBottom() + GameLevel.fillBottomLeft() + GameLevel.fillBottomRight(),

            SameMaxCirclesCountWinCondition(0.5f, 0.6f, 0.7f, 6),
            21
        ),

        13 to GameLevel( //stick
            listOf(
                Triangle(Point2D(-3f, 6f), Point2D(0f, 5f), Point2D(0f, 6f)),
                Triangle(Point2D(0f, 5f), Point2D(3f, -3f), Point2D(3f, 5f)),
                Triangle(Point2D(-3f, 3f), Point2D(-3f, -5f), Point2D(0f, -5f)),
                Triangle(Point2D(0f, -5f), Point2D(0f, -6f), Point2D(3f, -6f))
            ) + Quadrilateral(
                Point2D(-4.5f, 7f),
                Point2D(-4.5f, -7f),
                Point2D(-3f, -7f),
                Point2D(-3f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(3f, 7f),
                Point2D(3f, -7f),
                Point2D(4.5f, -7f),
                Point2D(4.5f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(-3f, 7f),
                Point2D(-3f, 6f),
                Point2D(3f, 6f),
                Point2D(3f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(0f, 6f),
                Point2D(0f, 5f),
                Point2D(3f, 5f),
                Point2D(3f, 6f)
            ).getTriangles() + Quadrilateral(
                Point2D(-3f, -5f),
                Point2D(-3f, -6f),
                Point2D(0f, -6f),
                Point2D(0f, -5f)
            ).getTriangles() + Quadrilateral(
                Point2D(-3f, -6f),
                Point2D(-3f, -7f),
                Point2D(3f, -7f),
                Point2D(3f, -6f)
            ).getTriangles() + GameLevel.fillAll(),

            SameMinSquareWinCondition(0.7f, 7, 6, 5),
            23
        ),

        14 to GameLevel( //arrow
            listOf(
                Triangle(Point2D(-3f, 6f), Point2D(-3f, 0f), Point2D(1f, 6f)),
                Triangle(Point2D(-3f, 0f), Point2D(-3f, -6f), Point2D(1f, -6f)),
                Triangle(Point2D(2f, 0f), Point2D(4f, -3f), Point2D(4f, 3f))
            ) + Quadrilateral(
                Point2D(-4.5f, 7f),
                Point2D(-4.5f, -7f),
                Point2D(-3f, -7f),
                Point2D(-3f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(4f, 7f),
                Point2D(4f, -7f),
                Point2D(4.5f, -7f),
                Point2D(4.5f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(-3f, 7f),
                Point2D(-3f, 6f),
                Point2D(4f, 6f),
                Point2D(4f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(-3f, -6f),
                Point2D(-3f, -7f),
                Point2D(4f, -7f),
                Point2D(4f, -6f)
            ).getTriangles() + GameLevel.fillAll(),

            SameMinSquareWinCondition(0.73f, 7, 6, 5),
            25
        ),

        15 to GameLevel( //romb
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
            ).getTriangles() + Quadrilateral(
                Point2D(-4.5f, 4.5f),
                Point2D(-4.5f, 7f),
                Point2D(4.5f, 7f),
                Point2D(4.5f, 4.5f)
            ).getTriangles() + GameLevel.fillAll(),

            SameMaxCirclesCountWinCondition(0.5f, 0.65f, 0.75f, 10),
            30
        ),

        16 to GameLevel( //romb in romb
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
            ).getTriangles() + Quadrilateral(
                Point2D(-4.5f, 4.5f),
                Point2D(-4.5f, 7f),
                Point2D(4.5f, 7f),
                Point2D(4.5f, 4.5f)
            ).getTriangles() + Quadrilateral(
                Point2D(0f, 1f),
                Point2D(-1f, 0f),
                Point2D(0f, -1f),
                Point2D(1f, 0f)
            ).getTriangles() + GameLevel.fillAll(),

            SameMaxCirclesCountWinCondition(0.5f, 0.6f, 0.7f, 8),
            35
        ),

        17 to GameLevel( //star
            listOf(
                Triangle(Point2D(-4.5f, 5f), Point2D(-4.5f, 1f), Point2D(-1.3f, 1f)),
                Triangle(Point2D(-4.5f, 5f), Point2D(-1.3f, 1f), Point2D(0f, 5f)),

                Triangle(Point2D(0f, 5f), Point2D(1.3f, 1f), Point2D(4.5f, 5f)),
                Triangle(Point2D(1.3f, 1f), Point2D(4.5f, 1f), Point2D(4.5f, 5f)),

                Triangle(Point2D(-4.5f, 1f), Point2D(-4.5f, -5f), Point2D(-1.8f, -1.2f)),
                Triangle(Point2D(-1.8f, -1.2f), Point2D(-4.5f, -5f), Point2D(-3f, -5f)),

                Triangle(Point2D(0f, -2.7f), Point2D(-3f, -5f), Point2D(3f, -5f)),

                Triangle(Point2D(1.8f, -1.2f), Point2D(3f, -5f), Point2D(4.5f, -5f)),
                Triangle(Point2D(1.8f, -1.2f), Point2D(4.5f, -5f), Point2D(4.5f, 1f))
            ) + Quadrilateral(
                Point2D(-4.5f, 7f),
                Point2D(-4.5f, 5f),
                Point2D(4.5f, 5f),
                Point2D(4.5f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(-4.5f, -5f),
                Point2D(-4.5f, -7f),
                Point2D(4.5f, -7f),
                Point2D(4.5f, -5f)
            ).getTriangles() + GameLevel.fillAll(),

            SameMaxCirclesCountWinCondition(0.4f, 0.5f, 0.6f, 8),
            40
        ),

        18 to GameLevel( //star not full
            listOf(
                Triangle(Point2D(-4.5f, 5f), Point2D(-4.5f, 1f), Point2D(-1.3f, 1f)),
                Triangle(Point2D(-4.5f, 5f), Point2D(-1.3f, 1f), Point2D(0f, 5f)),

                Triangle(Point2D(0f, 5f), Point2D(1.3f, 1f), Point2D(4.5f, 5f)),
                Triangle(Point2D(1.3f, 1f), Point2D(4.5f, 1f), Point2D(4.5f, 5f)),

                Triangle(Point2D(-4.5f, 1f), Point2D(-4.5f, -5f), Point2D(-1.8f, -1.2f)),
                Triangle(Point2D(-1.8f, -1.2f), Point2D(-4.5f, -5f), Point2D(-3f, -5f)),

                Triangle(Point2D(0f, -2.7f), Point2D(-3f, -5f), Point2D(3f, -5f)),

                Triangle(Point2D(1.8f, -1.2f), Point2D(3f, -5f), Point2D(4.5f, -5f)),
                Triangle(Point2D(1.8f, -1.2f), Point2D(4.5f, -5f), Point2D(4.5f, 1f)),

                Triangle(Point2D(-1.3f, 1f), Point2D(-1.8f, -1.2f), Point2D(1.8f, -1.2f)),
                Triangle(Point2D(-1.3f, 1f), Point2D(1.8f, -1.2f), Point2D(1.3f, 1f)),
                Triangle(Point2D(-1.8f, -1.2f), Point2D(0f, -2.7f), Point2D(1.8f, -1.2f))
            ) + Quadrilateral(
                Point2D(-4.5f, 7f),
                Point2D(-4.5f, 5f),
                Point2D(4.5f, 5f),
                Point2D(4.5f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(-4.5f, -5f),
                Point2D(-4.5f, -7f),
                Point2D(4.5f, -7f),
                Point2D(4.5f, -5f)
            ).getTriangles() + GameLevel.fillAll(),

            SameMaxCirclesCountWinCondition(0.36f, 0.41f, 0.46f, 10),
            45
        ),

        19 to GameLevel( //spiral
            listOf(
                Triangle(Point2D(1f, 0f), Point2D(0f, -1f), Point2D(1f, -2f)),
                Triangle(Point2D(1f, 0f), Point2D(1f, -2f), Point2D(3f, 2f)),
                Triangle(Point2D(0f, 1f), Point2D(1f, 0f), Point2D(3f, 2f)),
                Triangle(Point2D(-1f, 2f), Point2D(0f, 1f), Point2D(3f, 2f)),

                Triangle(Point2D(-2.5f, -1f), Point2D(0f, 1f), Point2D(-1f, 2f)),
                Triangle(Point2D(-4f, -1f), Point2D(-2.5f, -1f), Point2D(-1f, 2f)),

                Triangle(Point2D(-4f, -1f), Point2D(-1f, -7f), Point2D(-2.5f, -1f)),
                Triangle(Point2D(-2.5f, -1f), Point2D(-1f, -7f), Point2D(1f, -5f)),

                Triangle(Point2D(1f, -5f), Point2D(-1f, -7f), Point2D(4.5f, -7f)),
                Triangle(Point2D(1f, -5f), Point2D(4.5f, -7f), Point2D(4.5f, -2f)),

                Triangle(Point2D(-4.5f, 6f), Point2D(-4.5f, 4f), Point2D(-1f, 6f)),
                Triangle(Point2D(-1f, -7f), Point2D(4.5f, -21f), Point2D(4.5f, -7f))

            ) + Quadrilateral(
                Point2D(-13.5f, 7f),
                Point2D(-13.5f, 6f),
                Point2D(13.5f, 6f),
                Point2D(13.5f, 7f)
            ).getTriangles() + Quadrilateral(
                Point2D(-13.5f, 6f),
                Point2D(-13.5f, 4f),
                Point2D(-4.5f, 4f),
                Point2D(-4.5f, 6f)
            ).getTriangles() + Quadrilateral(
                Point2D(4.5f, -2f),
                Point2D(4.5f, -7f),
                Point2D(13.5f, -7f),
                Point2D(13.5f, -2f)
            ).getTriangles() + GameLevel.fillTop() + GameLevel.fillTopLeft() + GameLevel.fillTopRight() +
                    GameLevel.fillBottomRight(),

            SameMaxCirclesCountWinCondition(0.52f, 0.62f, 0.72f, 15),
            50
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