package org.example.eventspoc.event

data class UserCreatedEvent(val username: String, val email: String)

data class UserDeletedEvent(val username: String)
