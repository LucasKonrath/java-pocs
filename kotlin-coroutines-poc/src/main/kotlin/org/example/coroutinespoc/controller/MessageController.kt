package org.example.coroutinespoc.controller

import kotlinx.coroutines.delay
import org.example.coroutinespoc.model.Message
import org.springframework.web.bind.annotation.*
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/api/messages")
class MessageController {

    private val messages = ConcurrentHashMap<String, Message>()

    @GetMapping
    suspend fun getMessages(): List<Message> {
        delay(500)
        return messages.values.toList()
    }

    @GetMapping("/{id}")
    suspend fun getMessage(@PathVariable id: String): Message {
        delay(200)
        return messages[id] ?: throw NoSuchElementException("Message not found: $id")
    }

    @PostMapping
    suspend fun createMessage(@RequestBody message: Message): Message {
        val saved = message.copy(id = UUID.randomUUID().toString())
        messages[saved.id] = saved
        return saved
    }
}
