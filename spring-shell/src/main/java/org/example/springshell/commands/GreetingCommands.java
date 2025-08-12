package org.example.springshell.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class GreetingCommands {

    @ShellMethod(value = "Say hello to someone", key = {"hello", "hi"})
    public String hello(
            @ShellOption(defaultValue = "World") String name,
            @ShellOption(defaultValue = "1", help = "Number of times to greet") int times) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < times; i++) {
            result.append("Hello, ").append(name).append("!\n");
        }
        return result.toString().trim();
    }

    @ShellMethod(value = "Say goodbye", key = "goodbye")
    public String goodbye(@ShellOption(defaultValue = "Friend") String name) {
        return "Goodbye, " + name + "! Thanks for using Spring Shell!";
    }
}
