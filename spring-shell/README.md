# Spring Shell POC - Native Image Ready

A comprehensive Spring Shell proof-of-concept that demonstrates various command-line features and compiles to a native executable using GraalVM.

## Features

### Available Commands

#### Greeting Commands
- `hello [name] [--times=N]` - Say hello to someone (default: "World")
- `hi [name] [--times=N]` - Alias for hello
- `goodbye [name]` - Say goodbye (default: "Friend")

#### File System Commands
- `ls [directory]` - List files in directory (default: current directory)
- `list [directory]` - Alias for ls
- `pwd` - Show current working directory
- `current-dir` - Alias for pwd
- `mkdir <directory>` - Create a new directory
- `create-dir <directory>` - Alias for mkdir

#### Utility Commands
- `date [--format=pattern]` - Show current date/time with custom format
- `time [--format=pattern]` - Alias for date
- `calc <num1> <operation> <num2>` - Calculator (+, -, *, /, %, ^)
- `calculate <num1> <operation> <num2>` - Alias for calc
- `random [--min=N] [--max=N]` - Generate random number (default: 1-100)
- `rand [--min=N] [--max=N]` - Alias for random
- `history [--count=N]` - Show command history (default: last 10)
- `hist [--count=N]` - Alias for history
- `clear-history` - Clear all command history
- `sysinfo` - Display system information
- `system` - Alias for sysinfo

#### Built-in Commands
- `help` - Show all available commands
- `clear` - Clear the screen
- `exit` - Exit the application
- `quit` - Exit the application

## Building and Running

### Prerequisites
- Java 21+
- Maven 3.6+
- GraalVM (for native compilation)

### Run as Spring Boot Application
```bash
./mvnw spring-boot:run
```

### Build Native Executable
```bash
# Build native executable
./mvnw native:compile -Pnative

# Run the native executable
./target/spring-shell
```

### Build Native Docker Image
```bash
# Build lightweight container with native image
./mvnw spring-boot:build-image -Pnative

# Run the container
docker run --rm spring-shell:0.0.1-SNAPSHOT
```

## Example Usage

```bash
shell:> hello John --times=3
Hello, John!
Hello, John!
Hello, John!

shell:> calc 15 + 25
15.00 + 25.00 = 40.00

shell:> random --min=10 --max=50
Random number between 10 and 50: 37

shell:> date --format="yyyy-MM-dd HH:mm"
Current date and time: 2025-08-11 14:30

shell:> ls
Contents of .:
[DIR]  src
[DIR]  target
[FILE] pom.xml
[FILE] README.md

shell:> sysinfo
System Information:
OS Name: Mac OS X
OS Version: 14.5
Java Version: 21.0.2
...

shell:> history
Command History:
1. date command executed at 2025-08-11 14:30:15
2. calculation: 15.00 + 25.00 = 40.00
3. random number generated: 37
4. system info requested
```

## Native Image Benefits

- **Fast Startup**: Near-instantaneous application startup
- **Low Memory Usage**: Reduced memory footprint compared to JVM
- **Single Executable**: No need for Java runtime installation
- **Better Performance**: Optimized for command-line tools

## Technical Details

The project includes:
- **Runtime Hints**: Proper configuration for reflection-based operations
- **Native Image Compatibility**: All commands work seamlessly in native mode
- **Error Handling**: Comprehensive error handling for file operations
- **Memory Management**: Efficient memory usage for native compilation
- **Cross-Platform**: Works on Linux, macOS, and Windows (with appropriate builds)
