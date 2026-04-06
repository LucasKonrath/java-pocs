package org.example.grpcpoc.server;

import io.grpc.stub.StreamObserver;
import org.example.grpcpoc.generated.calculator.CalculationRequest;
import org.example.grpcpoc.generated.calculator.CalculationResponse;
import org.example.grpcpoc.generated.calculator.CalculatorServiceGrpc;
import org.example.grpcpoc.generated.calculator.NumberRequest;
import org.example.grpcpoc.generated.calculator.NumberResponse;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void add(CalculationRequest request, StreamObserver<CalculationResponse> responseObserver) {
        double result = request.getNum1() + request.getNum2();
        CalculationResponse response = CalculationResponse.newBuilder()
                .setResult(result)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void primeFactors(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        long number = request.getNumber();
        long n = Math.abs(number);

        while (n % 2 == 0) {
            responseObserver.onNext(NumberResponse.newBuilder().setFactor(2).build());
            n /= 2;
            sleep();
        }

        for (long i = 3; i * i <= n; i += 2) {
            while (n % i == 0) {
                responseObserver.onNext(NumberResponse.newBuilder().setFactor(i).build());
                n /= i;
                sleep();
            }
        }

        if (n > 1) {
            responseObserver.onNext(NumberResponse.newBuilder().setFactor(n).build());
        }

        responseObserver.onCompleted();
    }

    private void sleep() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
