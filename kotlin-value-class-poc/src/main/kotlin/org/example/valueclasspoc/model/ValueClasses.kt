package org.example.valueclasspoc.model

@JvmInline
value class Email(val value: String) {
    init {
        require("@" in value) { "Invalid email: must contain @" }
    }
}

@JvmInline
value class UserId(val value: Long)

@JvmInline
value class Money(val cents: Long) {
    fun toDollars(): Double = cents / 100.0
}
