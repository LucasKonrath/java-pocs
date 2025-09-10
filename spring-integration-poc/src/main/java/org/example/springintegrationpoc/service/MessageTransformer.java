package org.example.springintegrationpoc.service;

import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class MessageTransformer {

    @Transformer(inputChannel = "transformChannel", outputChannel = "inputChannel")
    public String transformToUpperCase(@Payload String payload) {
        System.out.println("Transforming message to uppercase: " + payload);
        return payload.toUpperCase();
    }

    @Transformer(inputChannel = "enrichChannel", outputChannel = "inputChannel")
    public String enrichMessage(@Payload String payload, @Headers Map<String, Object> headers) {
        System.out.println("Enriching message: " + payload);

        String enrichedMessage = String.format(
            "Message: %s | Timestamp: %s | Headers: %s",
            payload,
            LocalDateTime.now(),
            headers.toString()
        );

        return enrichedMessage;
    }

    @Transformer(inputChannel = "jsonTransformChannel", outputChannel = "inputChannel")
    public String transformToJson(@Payload String payload) {
        System.out.println("Transforming message to JSON format: " + payload);

        return String.format(
            "{\"message\": \"%s\", \"timestamp\": \"%s\", \"processed\": true}",
            payload.replace("\"", "\\\""),
            LocalDateTime.now()
        );
    }
}
