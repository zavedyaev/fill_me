package ru.zavedyaev.fillme.primitive

open class Point(
    val x: Float,
    val y: Float,
    val z: Float
) {
    companion object {
        const val COORDS_PER_VERTEX = 3
        const val BYTES_PER_VERTEX = 4
    }
}