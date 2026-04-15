package org.example.retrypoc.service

import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger

@Service
class UnstableService {

    private val attemptCounter = AtomicInteger(0)

    @Retryable(
        retryFor = [RuntimeException::class],
        maxAttempts = 5,
        backoff = Backoff(delay = 1000)
    )
    fun callUnstableOperation(): String {
        val attempt = attemptCounter.incrementAndGet()
        println("Attempt #$attempt")
        if (Math.random() < 0.6) {
            throw RuntimeException("Operation failed on attempt #$attempt")
        }
        attemptCounter.set(0)
        return "Success on attempt #$attempt"
    }

    @Recover
    fun recover(e: RuntimeException): String {
        attemptCounter.set(0)
        println("All retry attempts exhausted. Executing fallback.")
        return "Fallback response after all retries failed: ${e.message}"
    }
}
