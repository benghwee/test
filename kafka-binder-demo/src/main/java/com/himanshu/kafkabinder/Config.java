package com.himanshu.kafkabinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class Config {




    @Bean
    public Consumer<Message<?>> writeToFile(FileMessageHandler fileMessageHandler) {
        return message -> fileMessageHandler.handleMessage(message);
    }

    @Bean
    public FileMessageHandler fileMessageHandler(){
        return new FileMessageHandler();
    }
}
