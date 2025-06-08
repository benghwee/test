package com.himanshu.kafkabinder;


import example.avro.User;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.reflect.ReflectData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.apache.avro.generic.GenericRecord;

@SpringBootTest
//@DirtiesContext
//@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class SampleStreamTests {
    @Autowired
    private InputDestination input;
    @Autowired
    private OutputDestination output;
    @Autowired
    @Qualifier("avroSchemaMessageConverter")
    private MessageConverter messageConverter;
    @Test
    public void testEmptyConfiguration() {
        Person p = new Person("a","b",1);
        // get the reflected schema for packets
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);
        Object avroRecord = null;
        Message<?> avroMessage = messageConverter.toMessage(user1, null);
        this.input.send(avroMessage,"consumer1-topic");

        Message<byte[]> outMsg = output.receive(10000, "consumer1-topic");
        User user2 = (User) messageConverter.fromMessage(outMsg,User.class);
        System.out.println(user2.getFavoriteNumber());
        //Person p1 = (Person) output.receive(10000, "consumer1-topic"); // .getPayload();
        //assertThat(output.receive(10000, "consumer1-topic").getPayload()).isEqualTo("hello1".getBytes());
    }

    @SpringBootApplication
    @ImportAutoConfiguration({TestChannelBinderConfiguration.class})
    public static class SampleConfiguration {
        //@Bean
        //public Function<String, String> uppercase() {
            //return v -> v.toUpperCase();
        //}
        @Bean
        public Consumer<String> dummyFunction() {
            return message -> {
                // Do nothing
            };
        }
    }
}
