package org.example.grpcpoc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {

    private static final int PORT = 9090;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(PORT)
                .addService(new GreetingServiceImpl())
                .addService(new CalculatorServiceImpl())
                .build();

        server.start();
        System.out.println("gRPC Server started on port " + PORT);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gRPC server...");
            server.shutdown();
            System.out.println("Server shut down.");
        }));

        server.awaitTermination();
    }
}
