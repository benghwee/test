package com.himanshu.kafkabinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
@EnableKafka
@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(
        partitions = 1,
        controlledShutdown = false,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:3333",
                "port=3333"
        })
public class SampleStreamTest3 {
    @Autowired
    EmbeddedKafkaRule kafkaEmbeded;

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
}
