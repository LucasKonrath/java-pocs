# Spring Integration POC

This project demonstrates various Spring Integration patterns and concepts including message channels, transformers, filters, routers, service activators, and file integration.

## Features Demonstrated

### 1. Message Channels
- **DirectChannel**: Synchronous message processing
- Multiple channels for different processing flows

### 2. Message Processing Components
- **Service Activators**: Process messages and perform business logic
- **Transformers**: Transform message content (uppercase, JSON format, enrichment)
- **Filters**: Filter messages based on content criteria
- **Routers**: Route messages to different channels based on content

### 3. File Integration
- **File Inbound Adapter**: Automatically processes files from `./input` directory
- **File Outbound Adapter**: Writes processed content to `./output` directory
- Processes `.txt` files every 5 seconds

### 4. Gateway Pattern
- **MessageGateway**: Provides a clean interface for sending messages to integration flows
- Supports different transformation channels

### 5. Scheduled Processing
- **ScheduledMessageService**: Automatically generates and processes messages
- Demonstrates continuous message flow

## Architecture

```
HTTP Request → MessageController → MessageGateway → Transformation Channels → Processing Flow
                                                                               ↓
File Input → File Processing → Filter → Router → Channel-specific Handlers
```

## Message Flow Examples

### 1. Transform Flow
```
POST /api/messages/transform → transformChannel → transformer → inputChannel → processor → filter → router → handlers
```

### 2. Direct Flow
```
POST /api/messages/direct → inputChannel → processor → filter → router → handlers
```

### 3. File Flow
```
File in ./input → fileInputChannel → file processor → fileOutputChannel → ./output
```

## How to Run

1. **Start the application:**
   ```bash
   mvn spring-boot:run
   ```

2. **Test REST endpoints:**
   ```bash
   # Health check
   curl http://localhost:8080/api/messages/health
   
   # Send transform message
   curl -X POST http://localhost:8080/api/messages/transform \
        -H "Content-Type: text/plain" \
        -d "Hello Spring Integration"
   
   # Send direct message with type
   curl -X POST "http://localhost:8080/api/messages/direct?messageType=URGENT" \
        -H "Content-Type: text/plain" \
        -d "URGENT: System maintenance required"
   
   # Send enriched message
   curl -X POST "http://localhost:8080/api/messages/enrich?messageType=SYSTEM" \
        -H "Content-Type: text/plain" \
        -d "System status update"
   
   # Send JSON transform message
   curl -X POST http://localhost:8080/api/messages/json \
        -H "Content-Type: text/plain" \
        -d "Convert this to JSON"
   ```

3. **Test file integration:**
   - Place `.txt` files in the `./input` directory
   - Watch the console for processing logs
   - Check `./output` directory for processed files

## Message Types and Routing

The router directs messages to different handlers based on content:

- **URGENT messages** → `urgentChannel` → 🚨 URGENT MESSAGE HANDLER
- **NORMAL messages** → `normalChannel` → 📝 NORMAL MESSAGE HANDLER  
- **Other messages** → `defaultChannel` → 📄 DEFAULT MESSAGE HANDLER

## Filter Behavior

Messages containing "ERROR" are filtered out and won't reach the handlers.

## Scheduled Messages

The application automatically generates and processes messages:
- Every 10 seconds: Random message with routing
- Every 30 seconds: Transform message

## Sample Files

Two sample files are included in the `./input` directory:
- `sample1.txt`: Basic test file
- `urgent_alert.txt`: Urgent message file

## Key Spring Integration Annotations Used

- `@MessagingGateway`: Creates gateway interface
- `@ServiceActivator`: Defines service activation endpoints
- `@Transformer`: Defines message transformation logic
- `@Filter`: Defines message filtering logic
- `@Router`: Defines message routing logic
- `@InboundChannelAdapter`: Defines inbound adapters
- `@Poller`: Configures polling behavior

## Technologies Used

- Spring Boot 3.5.5
- Spring Integration
- Spring Web
- Maven

## Monitoring

Check the console output to see:
- Message processing logs
- Transformation operations
- Filter decisions
- Routing decisions
- File processing operations

The application runs on port 8080 by default.
