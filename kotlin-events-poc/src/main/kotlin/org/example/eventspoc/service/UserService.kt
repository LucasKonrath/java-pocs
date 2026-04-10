package org.example.eventspoc.service

import org.example.eventspoc.event.UserCreatedEvent
import org.example.eventspoc.event.UserDeletedEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class UserService(private val eventPublisher: ApplicationEventPublisher) {

    fun createUser(username: String, email: String) {
        eventPublisher.publishEvent(UserCreatedEvent(username, email))
    }

    fun deleteUser(username: String) {
        eventPublisher.publishEvent(UserDeletedEvent(username))
    }
}
