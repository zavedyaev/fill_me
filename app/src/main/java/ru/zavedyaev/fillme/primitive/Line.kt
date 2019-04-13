package ru.zavedyaev.fillme.primitive

class Line(
    val a: Point,
    val b: Point
) {
    fun getLength(): Float {
        return Math.sqrt(
            Math.pow((a.x - b.x).toDouble(), 2.0) +
                    Math.pow((a.y - b.y).toDouble(), 2.0) +
                    Math.pow((a.z - b.z).toDouble(), 2.0)
        ).toFloat()
    }
}