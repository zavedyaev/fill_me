package ru.zavedyaev.fillme.primitive

import com.fasterxml.jackson.annotation.JsonIgnore

open class Circle(
    val center: Point,
    val normal: Point,
    val radius: Float
) {
    @JsonIgnore
    fun getSquare(): Float = (Math.PI * radius * radius).toFloat()
}