package org.example.springintegrationpoc.config;

import org.example.springintegrationpoc.service.MessageProcessor;
import org.example.springintegrationpoc.service.MessageTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel processedChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel fileInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel fileOutputChannel() {
        return new DirectChannel();
    }

    // Additional channels for different transformation flows
    @Bean
    public MessageChannel transformChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel enrichChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel jsonTransformChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel urgentChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel normalChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel defaultChannel() {
        return new DirectChannel();
    }

    // File input adapter - reads files from input directory
    @Bean
    @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "5000"))
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("./input"));
        source.setFilter(new SimplePatternFileListFilter("*.txt"));
        return source;
    }

    // File output adapter - writes processed files to output directory
    @Bean
    @ServiceActivator(inputChannel = "fileOutputChannel")
    public MessageHandler fileWritingMessageHandler() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("./output"));
        handler.setFileExistsMode(FileExistsMode.REPLACE);
        handler.setExpectReply(false);
        return handler;
    }

    @Bean
    public MessageProcessor messageProcessor() {
        return new MessageProcessor();
    }

    @Bean
    public MessageTransformer messageTransformer() {
        return new MessageTransformer();
    }
}
