package org.example.schedulingpoc.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.management.ManagementFactory
import java.time.Duration

@RestController
@RequestMapping("/api")
class TaskController {

    @GetMapping("/status")
    fun status(): Map<String, String> {
        val uptime = Duration.ofMillis(ManagementFactory.getRuntimeMXBean().uptime)
        return mapOf(
            "status" to "running",
            "uptime" to "${uptime.toHours()}h ${uptime.toMinutesPart()}m ${uptime.toSecondsPart()}s"
        )
    }
}
