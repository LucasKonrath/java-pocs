package org.example.grpcpoc.server;

import io.grpc.stub.StreamObserver;
import org.example.grpcpoc.generated.greeting.GreetingServiceGrpc;
import org.example.grpcpoc.generated.greeting.HelloRequest;
import org.example.grpcpoc.generated.greeting.HelloResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String greeting = buildGreeting(request.getName(), request.getLanguage());
        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .setTimestamp(LocalDateTime.now().toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sayHelloServerStream(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String name = request.getName();
        String[] greetings = {"Hello", "Hi", "Hey", "Greetings", "Welcome"};

        for (String greet : greetings) {
            HelloResponse response = HelloResponse.newBuilder()
                    .setGreeting(greet + ", " + name + "!")
                    .setTimestamp(LocalDateTime.now().toString())
                    .build();
            responseObserver.onNext(response);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<HelloRequest> sayHelloClientStream(StreamObserver<HelloResponse> responseObserver) {
        List<String> names = new ArrayList<>();

        return new StreamObserver<>() {
            @Override
            public void onNext(HelloRequest request) {
                names.add(request.getName());
                System.out.println("[Server] Received name: " + request.getName());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("[Server] Error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                String combined = String.join(", ", names);
                HelloResponse response = HelloResponse.newBuilder()
                        .setGreeting("Hello to all: " + combined + "!")
                        .setTimestamp(LocalDateTime.now().toString())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    private String buildGreeting(String name, String language) {
        return switch (language.toLowerCase()) {
            case "pt" -> "Olá, " + name + "!";
            case "es" -> "¡Hola, " + name + "!";
            default -> "Hello, " + name + "!";
        };
    }
}
