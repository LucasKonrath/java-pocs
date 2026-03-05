package org.example.jdk25;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class ScopedValues {
    static final ScopedValue<String> id = ScopedValue.newInstance();
    static final ScopedValue<String> anotherValue = ScopedValue.newInstance();


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Runnable task = () -> {
            if(id.isBound()) {
                System.out.println("ID: " + id.get());
            } else {
                System.out.println("ID is not bound");
            }
        };

        System.out.println("--- Simple Example ---");
        ScopedValue.where(id, "12345").run(task);
        task.run();


        System.out.println("\n--- Nested Scopes Example ---");
        nestedScopesExample();

        System.out.println("\n--- Callable Example ---");
        callableExample();

        System.out.println("\n--- Inheritance Example with StructuredTaskScope ---");
        inheritanceExample();
    }

    static void nestedScopesExample() {
        ScopedValue.where(id, "outer").run(() -> {
            System.out.println("Outer scope: " + id.get());
            ScopedValue.where(id, "inner").run(() -> {
                System.out.println("Inner scope: " + id.get());
            });
            System.out.println("Outer scope after inner: " + id.get());
        });
    }

    static void callableExample() throws ExecutionException, InterruptedException {
        Callable<String> task = () -> {
            if (id.isBound()) {
                return "ID " + id.get() + " is bound";
            }
            return "ID is not bound";
        };

        String result = ScopedValue.where(id, "test-id").call(task);
        System.out.println("Callable result: " + result);

        String resultUnbound = ScopedValue.call(task);
        System.out.println("Callable result (unbound): " + resultUnbound);
    }

    static void inheritanceExample() throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            ScopedValue.where(id, "structured-task").run(() -> {
                scope.fork(() -> {
                    System.out.println("Child thread ID: " + id.get());
                    return null;
                });
            });
            scope.join().throwIfFailed();
        }
    }
}
