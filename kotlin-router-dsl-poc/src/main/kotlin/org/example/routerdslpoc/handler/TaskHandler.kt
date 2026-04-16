package org.example.routerdslpoc.handler

import org.example.routerdslpoc.model.Task
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class TaskHandler {

    private val tasks = ConcurrentHashMap<String, Task>()

    fun listAll(request: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().bodyValue(tasks.values.toList())

    fun getById(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        val task = tasks[id]
        return if (task != null) {
            ServerResponse.ok().bodyValue(task)
        } else {
            ServerResponse.notFound().build()
        }
    }

    fun create(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(Task::class.java)
            .map { it.copy(id = UUID.randomUUID().toString(), completed = false) }
            .flatMap { task ->
                tasks[task.id] = task
                ServerResponse.ok().bodyValue(task)
            }

    fun toggleComplete(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        val task = tasks[id]
        return if (task != null) {
            val toggled = task.copy(completed = !task.completed)
            tasks[id] = toggled
            ServerResponse.ok().bodyValue(toggled)
        } else {
            ServerResponse.notFound().build()
        }
    }
}
