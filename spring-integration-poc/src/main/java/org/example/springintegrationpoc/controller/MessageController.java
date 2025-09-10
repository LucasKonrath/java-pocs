package org.example.springintegrationpoc.controller;

import org.example.springintegrationpoc.gateway.MessageGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageGateway messageGateway;

    @PostMapping("/transform")
    public String sendTransformMessage(@RequestBody String message) {
        messageGateway.sendToTransformChannel(message);
        return "Message sent to transform channel: " + message;
    }

    @PostMapping("/enrich")
    public String sendEnrichMessage(@RequestBody String message, @RequestParam String messageType) {
        messageGateway.sendToEnrichChannel(message, messageType);
        return "Message sent to enrich channel: " + message + " with type: " + messageType;
    }

    @PostMapping("/json")
    public String sendJsonTransformMessage(@RequestBody String message) {
        messageGateway.sendToJsonTransformChannel(message);
        return "Message sent to JSON transform channel: " + message;
    }

    @PostMapping("/direct")
    public String sendDirectMessage(@RequestBody String message, @RequestParam String messageType) {
        messageGateway.sendDirectMessage(message, messageType);
        return "Message sent directly to input channel: " + message + " with type: " + messageType;
    }

    @GetMapping("/health")
    public String health() {
        return "Spring Integration POC is running!";
    }
}
