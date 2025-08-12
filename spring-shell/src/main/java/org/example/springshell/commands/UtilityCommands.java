package org.example.springshell.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ShellComponent
public class UtilityCommands {

    private final List<String> history = new ArrayList<>();
    private final Random random = new Random();

    @ShellMethod(value = "Get current date and time", key = {"date", "time"})
    public String currentDateTime(@ShellOption(defaultValue = "yyyy-MM-dd HH:mm:ss") String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            String dateTime = LocalDateTime.now().format(formatter);
            history.add("date command executed at " + dateTime);
            return "Current date and time: " + dateTime;
        } catch (Exception e) {
            return "Invalid date format. Use patterns like 'yyyy-MM-dd HH:mm:ss'";
        }
    }

    @ShellMethod(value = "Calculate simple math operations", key = {"calc", "calculate"})
    public String calculate(double num1, String operation, double num2) {
        double result;
        try {
            switch (operation.toLowerCase()) {
                case "+", "add" -> result = num1 + num2;
                case "-", "subtract" -> result = num1 - num2;
                case "*", "multiply" -> result = num1 * num2;
                case "/", "divide" -> {
                    if (num2 == 0) {
                        return "Error: Division by zero";
                    }
                    result = num1 / num2;
                }
                case "%", "mod" -> result = num1 % num2;
                case "^", "power" -> result = Math.pow(num1, num2);
                default -> {
                    return "Unsupported operation. Use: +, -, *, /, %, ^";
                }
            }

            String calculation = String.format("%.2f %s %.2f = %.2f", num1, operation, num2, result);
            history.add("calculation: " + calculation);
            return calculation;
        } catch (Exception e) {
            return "Error performing calculation: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Generate a random number", key = {"random", "rand"})
    public String randomNumber(
            @ShellOption(defaultValue = "1") int min,
            @ShellOption(defaultValue = "100") int max) {

        if (min >= max) {
            return "Error: min value must be less than max value";
        }

        int randomNum = random.nextInt(max - min + 1) + min;
        String result = "Random number between " + min + " and " + max + ": " + randomNum;
        history.add("random number generated: " + randomNum);
        return result;
    }

    @ShellMethod(value = "Show command history", key = {"history", "hist"})
    public String showHistory(@ShellOption(defaultValue = "10") int count) {
        if (history.isEmpty()) {
            return "No command history available";
        }

        StringBuilder result = new StringBuilder("Command History:\n");
        int start = Math.max(0, history.size() - count);

        for (int i = start; i < history.size(); i++) {
            result.append(String.format("%d. %s\n", i + 1, history.get(i)));
        }

        return result.toString();
    }

    @ShellMethod(value = "Clear command history", key = {"clear-history"})
    public String clearHistory() {
        int size = history.size();
        history.clear();
        return "Cleared " + size + " history entries";
    }

    @ShellMethod(value = "Get system information", key = {"sysinfo", "system"})
    public String systemInfo() {
        StringBuilder info = new StringBuilder();
        info.append("System Information:\n");
        info.append("OS Name: ").append(System.getProperty("os.name")).append("\n");
        info.append("OS Version: ").append(System.getProperty("os.version")).append("\n");
        info.append("OS Architecture: ").append(System.getProperty("os.arch")).append("\n");
        info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        info.append("Java Vendor: ").append(System.getProperty("java.vendor")).append("\n");
        info.append("User Name: ").append(System.getProperty("user.name")).append("\n");
        info.append("User Home: ").append(System.getProperty("user.home")).append("\n");

        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        long totalMemory = runtime.totalMemory() / (1024 * 1024);
        long freeMemory = runtime.freeMemory() / (1024 * 1024);

        info.append("Max Memory: ").append(maxMemory).append(" MB\n");
        info.append("Total Memory: ").append(totalMemory).append(" MB\n");
        info.append("Free Memory: ").append(freeMemory).append(" MB\n");

        history.add("system info requested");
        return info.toString();
    }
}
