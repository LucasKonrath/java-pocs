package org.example.sealedpoc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SealedPocApplication

fun main(args: Array<String>) {
    runApplication<SealedPocApplication>(*args)
}
