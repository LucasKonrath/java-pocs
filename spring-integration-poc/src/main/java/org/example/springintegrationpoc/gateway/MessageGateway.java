package org.example.springintegrationpoc.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway
public interface MessageGateway {

    @Gateway(requestChannel = "transformChannel")
    void sendToTransformChannel(@Payload String message);

    @Gateway(requestChannel = "enrichChannel")
    void sendToEnrichChannel(@Payload String message, @Header("messageType") String messageType);

    @Gateway(requestChannel = "jsonTransformChannel")
    void sendToJsonTransformChannel(@Payload String message);

    @Gateway(requestChannel = "inputChannel")
    void sendDirectMessage(@Payload String message, @Header("messageType") String messageType);
}
