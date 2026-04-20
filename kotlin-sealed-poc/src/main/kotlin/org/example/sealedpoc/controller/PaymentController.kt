package org.example.sealedpoc.controller

import org.example.sealedpoc.model.PaymentResult
import org.example.sealedpoc.model.Shape
import org.example.sealedpoc.model.area
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api")
class PaymentController {

    @PostMapping("/payments")
    fun processPayment(@RequestBody request: Map<String, Any>): PaymentResult {
        val amount = (request["amount"] as? Number)?.toDouble() ?: 100.0
        return when ((0..2).random()) {
            0 -> PaymentResult.Success(
                transactionId = UUID.randomUUID().toString(),
                amount = amount
            )
            1 -> PaymentResult.Declined(reason = "Insufficient funds")
            else -> PaymentResult.Error(exception = "Payment gateway timeout")
        }
    }

    @GetMapping("/shapes/area")
    fun calculateArea(
        @RequestParam type: String,
        @RequestParam(required = false) radius: Double?,
        @RequestParam(required = false) width: Double?,
        @RequestParam(required = false) height: Double?,
        @RequestParam(required = false) base: Double?
    ): Map<String, Any> {
        val shape: Shape = when (type.lowercase()) {
            "circle" -> Shape.Circle(radius ?: throw IllegalArgumentException("radius is required"))
            "rectangle" -> Shape.Rectangle(
                width ?: throw IllegalArgumentException("width is required"),
                height ?: throw IllegalArgumentException("height is required")
            )
            "triangle" -> Shape.Triangle(
                base ?: throw IllegalArgumentException("base is required"),
                height ?: throw IllegalArgumentException("height is required")
            )
            else -> throw IllegalArgumentException("Unknown shape type: $type")
        }
        return mapOf("shape" to type, "area" to shape.area())
    }
}
