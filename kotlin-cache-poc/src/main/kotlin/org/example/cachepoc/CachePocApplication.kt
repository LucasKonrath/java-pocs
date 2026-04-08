package org.example.cachepoc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class CachePocApplication

fun main(args: Array<String>) {
    runApplication<CachePocApplication>(*args)
}
