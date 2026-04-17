package org.example.schedulingpoc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class SchedulingPocApplication

fun main(args: Array<String>) {
    runApplication<SchedulingPocApplication>(*args)
}
