package org.example.sealedpoc.model

import kotlin.math.sqrt

sealed interface Shape {
    data class Circle(val radius: Double) : Shape
    data class Rectangle(val width: Double, val height: Double) : Shape
    data class Triangle(val base: Double, val height: Double) : Shape
}

fun Shape.area(): Double = when (this) {
    is Shape.Circle -> Math.PI * radius * radius
    is Shape.Rectangle -> width * height
    is Shape.Triangle -> 0.5 * base * height
}
