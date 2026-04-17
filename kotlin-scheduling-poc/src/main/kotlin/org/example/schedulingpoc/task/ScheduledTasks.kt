package org.example.schedulingpoc.task

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ScheduledTasks {

    @Scheduled(fixedRate = 5000)
    fun printTimestamp() {
        println("Current timestamp: ${LocalDateTime.now()}")
    }

    @Scheduled(cron = "0/15 * * * * *")
    fun cleanup() {
        println("Cleanup running")
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 3000)
    fun heartbeat() {
        println("Heartbeat pulse at ${LocalDateTime.now()}")
    }
}
