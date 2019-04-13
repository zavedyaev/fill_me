package ru.zavedyaev.fillme.primitive

open class Circle(
    val center: Point,
    val normal: Point,
    val radius: Float
) {
    fun getSquare(): Float = (Math.PI * radius * radius).toFloat()
}