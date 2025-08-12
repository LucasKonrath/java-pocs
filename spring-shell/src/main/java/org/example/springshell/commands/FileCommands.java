package org.example.springshell.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@ShellComponent
public class FileCommands {

    @ShellMethod(value = "List files in directory", key = {"ls", "list"})
    public String listFiles(@ShellOption(defaultValue = ".") String directory) {
        try {
            Path path = Paths.get(directory);
            if (!Files.exists(path)) {
                return "Directory does not exist: " + directory;
            }

            StringBuilder result = new StringBuilder();
            result.append("Contents of ").append(directory).append(":\n");

            try (Stream<Path> files = Files.list(path)) {
                files.forEach(file -> {
                    String type = Files.isDirectory(file) ? "[DIR]" : "[FILE]";
                    result.append(String.format("%-6s %s\n", type, file.getFileName()));
                });
            }

            return result.toString();
        } catch (IOException e) {
            return "Error listing directory: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Show current working directory", key = {"pwd", "current-dir"})
    public String currentDirectory() {
        return "Current directory: " + System.getProperty("user.dir");
    }

    @ShellMethod(value = "Create a new directory", key = {"mkdir", "create-dir"})
    public String createDirectory(String directoryName) {
        try {
            Path path = Paths.get(directoryName);
            Files.createDirectories(path);
            return "Directory created: " + directoryName;
        } catch (IOException e) {
            return "Error creating directory: " + e.getMessage();
        }
    }
}
