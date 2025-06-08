//package com.himanshu.kafkabinder;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//
//
//@Component
//public class KafkaProducer {
//    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
//
//    @Autowired
//    private KafkaTemplate<String, byte[]> kafkaTemplate;
//
//    public void send(String topic, byte[] payload) {
//        LOGGER.info("sending payload='{}' to topic='{}'", payload, topic);
//        kafkaTemplate.send(topic, payload);
//    }
//}
