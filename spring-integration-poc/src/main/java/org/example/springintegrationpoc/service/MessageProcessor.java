package org.example.springintegrationpoc.service;

import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MessageProcessor {

    @ServiceActivator(inputChannel = "inputChannel", outputChannel = "processedChannel")
    public String processMessage(@Payload String payload, @Header(value = "messageType", required = false) String messageType) {
        System.out.println("Processing message: " + payload);
        System.out.println("Message type: " + messageType);

        // Simulate some processing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "PROCESSED: " + payload.toUpperCase();
    }

    @Filter(inputChannel = "processedChannel", outputChannel = "outputChannel")
    public boolean filterMessage(@Payload String payload) {
        // Filter out messages that contain "ERROR"
        boolean shouldPass = !payload.contains("ERROR");
        System.out.println("Filtering message: " + payload + " -> " + (shouldPass ? "PASSED" : "FILTERED"));
        return shouldPass;
    }

    @Router(inputChannel = "outputChannel")
    public String routeMessage(@Payload String payload) {
        if (payload.contains("URGENT")) {
            return "urgentChannel";
        } else if (payload.contains("NORMAL")) {
            return "normalChannel";
        } else {
            return "defaultChannel";
        }
    }

    @ServiceActivator(inputChannel = "urgentChannel")
    public void handleUrgentMessage(@Payload String payload) {
        System.out.println("🚨 URGENT MESSAGE HANDLER: " + payload);
    }

    @ServiceActivator(inputChannel = "normalChannel")
    public void handleNormalMessage(@Payload String payload) {
        System.out.println("📝 NORMAL MESSAGE HANDLER: " + payload);
    }

    @ServiceActivator(inputChannel = "defaultChannel")
    public void handleDefaultMessage(@Payload String payload) {
        System.out.println("📄 DEFAULT MESSAGE HANDLER: " + payload);
    }

    @ServiceActivator(inputChannel = "fileInputChannel", outputChannel = "fileOutputChannel")
    public String processFileContent(@Payload byte[] fileContent, @Header("file_name") String fileName) {
        String content = new String(fileContent);
        System.out.println("Processing file: " + fileName);
        System.out.println("File content length: " + content.length());

        // Transform file content
        String processedContent = "=== PROCESSED FILE: " + fileName + " ===\n" +
                                 "Original content:\n" + content + "\n" +
                                 "=== Processing completed at: " + java.time.LocalDateTime.now() + " ===\n";

        return processedContent;
    }
}
