package org.example.jdk25;

import java.util.concurrent.StructuredTaskScope;

public class StructuredTasks {
    public static void main(String[] args) {
        try (var scope = StructuredTaskScope.<String>open()) {
            var future1 = scope.fork(() -> {
                Thread.sleep(1000);
                return "Task 1 completed";
            });

            var future2 = scope.fork(() -> {
                Thread.sleep(2000);
                return "Task 2 completed";
            });

            scope.join();  // Wait for all tasks to complete
            System.out.println(future1.get() + ", " + future2.get());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Tasks were interrupted");
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
