package org.example.routerdslpoc.router

import org.example.routerdslpoc.handler.TaskHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class TaskRouter {

    @Bean
    fun taskRoutes(handler: TaskHandler) = router {
        "/api/tasks".nest {
            GET("", handler::listAll)
            GET("/{id}", handler::getById)
            POST("", handler::create)
            PUT("/{id}/toggle", handler::toggleComplete)
        }
    }
}
