package org.example.springshell.config;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(NativeHintsConfiguration.SpringShellRuntimeHints.class)
public class NativeHintsConfiguration {

    static class SpringShellRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            // Register command classes for reflection
            hints.reflection()
                .registerType(org.example.springshell.commands.GreetingCommands.class)
                .registerType(org.example.springshell.commands.FileCommands.class)
                .registerType(org.example.springshell.commands.UtilityCommands.class);

            // Register resource patterns that might be needed
            hints.resources().registerPattern("*.properties");
            hints.resources().registerPattern("banner.txt");
        }
    }
}
