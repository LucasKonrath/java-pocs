package org.example.retrypoc.controller

import org.example.retrypoc.service.UnstableService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class RetryController(private val unstableService: UnstableService) {

    @GetMapping("/retry")
    fun retry(): String {
        return unstableService.callUnstableOperation()
    }
}
