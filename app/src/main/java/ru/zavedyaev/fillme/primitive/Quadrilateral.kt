package ru.zavedyaev.fillme.primitive

/**
 * Represents a quadrilateral
 *      a--d
 *     /    \
 *    /     \
 *   b------c
 */
open class Quadrilateral(
    val a: Point,
    val b: Point,
    val c: Point,
    val d: Point
) {
    private val abcTriangle = Triangle(a, b, c)
    private val acdTriangle = Triangle(a, c, d)

    fun getSquare(): Float {
        return abcTriangle.getSquare() + acdTriangle.getSquare()
    }

    fun draw(positionHandle: Int, colorHandle: Int, textureHandle: Int, color1: FloatArray, color2: FloatArray) {
        abcTriangle.draw(positionHandle, colorHandle, textureHandle, color1)
        acdTriangle.draw(positionHandle, colorHandle, textureHandle, color2)
    }

    fun getTriangles(): List<Triangle> = listOf(abcTriangle, acdTriangle)
}