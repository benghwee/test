package com.himanshu.kafkabinder;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class FileMessageHandler implements MessageHandler {
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        System.out.println("1 message arrived");
        System.out.println(message);
        throw new RuntimeException("error");
    }
}
