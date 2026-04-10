package org.example.eventspoc.listener

import org.example.eventspoc.event.UserCreatedEvent
import org.example.eventspoc.event.UserDeletedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class UserEventListener {

    @EventListener
    fun handleUserCreated(event: UserCreatedEvent) {
        println("User created: ${event.username} (${event.email})")
    }

    @EventListener
    fun handleUserDeleted(event: UserDeletedEvent) {
        println("User deleted: ${event.username}")
    }
}
