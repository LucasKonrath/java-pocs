package org.example.jdk25;

public class ScopedValues {
    static final ScopedValue<String> id = ScopedValue.newInstance();

    public static void main(String[] args) {
        Runnable task = () -> {
            System.out.println("ID: " + id.get());
        };

        ScopedValue.where(id, "12345").run(task);
    }
}
