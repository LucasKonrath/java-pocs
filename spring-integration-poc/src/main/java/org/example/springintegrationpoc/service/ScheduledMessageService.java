package org.example.springintegrationpoc.service;

import org.example.springintegrationpoc.gateway.MessageGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class ScheduledMessageService {

    @Autowired
    private MessageGateway messageGateway;

    private final Random random = new Random();
    private final List<String> sampleMessages = Arrays.asList(
        "URGENT: System alert detected",
        "NORMAL: Daily health check",
        "Processing batch job 123",
        "ERROR: Database connection failed",
        "User login successful",
        "URGENT: Security breach detected",
        "NORMAL: Backup completed successfully"
    );

    private final List<String> messageTypes = Arrays.asList(
        "SYSTEM", "USER", "BATCH", "SECURITY", "MAINTENANCE"
    );

    @Scheduled(fixedRate = 10000) // Every 10 seconds
    public void sendScheduledMessage() {
        String message = sampleMessages.get(random.nextInt(sampleMessages.size()));
        String messageType = messageTypes.get(random.nextInt(messageTypes.size()));

        System.out.println("🕒 Scheduled message service sending: " + message);
        messageGateway.sendDirectMessage(message + " - " + LocalDateTime.now(), messageType);
    }

    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void sendTransformMessage() {
        String message = "scheduled transform message " + System.currentTimeMillis();
        System.out.println("🔄 Scheduled transform message: " + message);
        messageGateway.sendToTransformChannel(message);
    }
}
