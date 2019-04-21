package ru.zavedyaev.fillme.primitive

open class Point(
    val x: Float,
    val y: Float,
    val z: Float
) {
    companion object {
        const val COORDS_PER_POINT = 3
        const val BYTES_PER_FLOAT = 4
        const val BYTES_PER_POINT = COORDS_PER_POINT * BYTES_PER_FLOAT
    }
}