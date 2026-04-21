package org.example.valueclasspoc.model

data class User(
    val id: UserId,
    val name: String,
    val email: Email,
    val balance: Money
)
