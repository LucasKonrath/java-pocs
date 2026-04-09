package org.example.coroutinespoc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoroutinesPocApplication

fun main(args: Array<String>) {
    runApplication<CoroutinesPocApplication>(*args)
}
