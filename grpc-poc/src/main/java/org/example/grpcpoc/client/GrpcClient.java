package org.example.grpcpoc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpcpoc.generated.calculator.CalculationRequest;
import org.example.grpcpoc.generated.calculator.CalculationResponse;
import org.example.grpcpoc.generated.calculator.CalculatorServiceGrpc;
import org.example.grpcpoc.generated.calculator.NumberRequest;
import org.example.grpcpoc.generated.calculator.NumberResponse;
import org.example.grpcpoc.generated.greeting.GreetingServiceGrpc;
import org.example.grpcpoc.generated.greeting.HelloRequest;
import org.example.grpcpoc.generated.greeting.HelloResponse;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GrpcClient {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        try {
            unaryGreeting(channel);
            serverStreamingGreeting(channel);
            clientStreamingGreeting(channel);
            unaryAdd(channel);
            serverStreamingPrimeFactors(channel);
        } finally {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private static void unaryGreeting(ManagedChannel channel) {
        System.out.println("\n=== Unary: SayHello ===");
        var stub = GreetingServiceGrpc.newBlockingStub(channel);

        for (String lang : new String[]{"en", "pt", "es"}) {
            HelloResponse response = stub.sayHello(
                    HelloRequest.newBuilder().setName("Lucas").setLanguage(lang).build());
            System.out.println("[%s] %s (at %s)".formatted(lang, response.getGreeting(), response.getTimestamp()));
        }
    }

    private static void serverStreamingGreeting(ManagedChannel channel) {
        System.out.println("\n=== Server Streaming: SayHelloServerStream ===");
        var stub = GreetingServiceGrpc.newBlockingStub(channel);

        Iterator<HelloResponse> responses = stub.sayHelloServerStream(
                HelloRequest.newBuilder().setName("Lucas").setLanguage("en").build());

        while (responses.hasNext()) {
            HelloResponse response = responses.next();
            System.out.println("Stream: %s (at %s)".formatted(response.getGreeting(), response.getTimestamp()));
        }
    }

    private static void clientStreamingGreeting(ManagedChannel channel) throws InterruptedException {
        System.out.println("\n=== Client Streaming: SayHelloClientStream ===");
        var stub = GreetingServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<HelloRequest> requestObserver = stub.sayHelloClientStream(new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse response) {
                System.out.println("Response: %s".formatted(response.getGreeting()));
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: " + t.getMessage());
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Client streaming completed.");
                latch.countDown();
            }
        });

        for (String name : new String[]{"Alice", "Bob", "Carol"}) {
            System.out.println("Sending name: " + name);
            requestObserver.onNext(HelloRequest.newBuilder().setName(name).setLanguage("en").build());
            Thread.sleep(300);
        }
        requestObserver.onCompleted();
        latch.await(5, TimeUnit.SECONDS);
    }

    private static void unaryAdd(ManagedChannel channel) {
        System.out.println("\n=== Unary: Calculator Add ===");
        var stub = CalculatorServiceGrpc.newBlockingStub(channel);

        CalculationResponse response = stub.add(
                CalculationRequest.newBuilder().setNum1(15.5).setNum2(24.3).build());
        System.out.println("15.5 + 24.3 = " + response.getResult());
    }

    private static void serverStreamingPrimeFactors(ManagedChannel channel) {
        System.out.println("\n=== Server Streaming: Prime Factors ===");
        var stub = CalculatorServiceGrpc.newBlockingStub(channel);

        long number = 120;
        System.out.print("Prime factors of %d: ".formatted(number));

        Iterator<NumberResponse> factors = stub.primeFactors(
                NumberRequest.newBuilder().setNumber(number).build());

        while (factors.hasNext()) {
            System.out.print(factors.next().getFactor() + " ");
        }
        System.out.println();
    }
}
