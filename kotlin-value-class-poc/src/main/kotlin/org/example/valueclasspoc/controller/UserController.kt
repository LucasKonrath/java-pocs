package org.example.valueclasspoc.controller

import org.example.valueclasspoc.model.Email
import org.example.valueclasspoc.model.Money
import org.example.valueclasspoc.model.User
import org.example.valueclasspoc.model.UserId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController {

    @GetMapping
    fun getUsers(): List<User> = listOf(
        User(UserId(1), "Alice", Email("alice@example.com"), Money(10050)),
        User(UserId(2), "Bob", Email("bob@example.com"), Money(25099))
    )

    @PostMapping("/validate")
    fun validateEmail(@RequestBody body: Map<String, String>): ResponseEntity<Map<String, Any>> {
        val emailStr = body["email"] ?: return ResponseEntity.badRequest()
            .body(mapOf("valid" to false, "error" to "email field is required"))
        return try {
            Email(emailStr)
            ResponseEntity.ok(mapOf("valid" to true, "email" to emailStr))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("valid" to false, "error" to e.message.orEmpty()))
        }
    }
}
