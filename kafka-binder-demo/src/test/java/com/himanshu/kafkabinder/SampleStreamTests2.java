package com.himanshu.kafkabinder;

import example.avro.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.function.context.config.SmartCompositeMessageConverter;
import org.springframework.cloud.function.context.converter.avro.AvroSchemaMessageConverter;
import org.springframework.cloud.function.context.converter.avro.AvroSchemaServiceManager;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@ActiveProfiles("test")
//@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class SampleStreamTests2 {

    @Autowired
    @Qualifier("avroSchemaMessageConverter")
    private MessageConverter messageConverter;
    @Test
    public void testMultipleFunctions2() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        SampleFunctionConfiguration.class))
                .run(
                        "--spring.cloud.function.definition=uppercase;reverse",
                        "--spring.cloud.stream.bindings.uppercase-in-0.destination=myInput",
                        "--spring.cloud.stream.bindings.uppercase-in-0.content-type=application/avro",
                        "--spring.cloud.stream.bindings.uppercase-out-0.destination=myInput_",
                        "--spring.cloud.stream.bindings.uppercase-out-0.content-type=application/avro"
                )) {

            InputDestination inputDestination = context.getBean(InputDestination.class);

            OutputDestination outputDestination = context.getBean(OutputDestination.class);
            User user1 = new User();
            user1.setName("Alyssa");
            user1.setFavoriteNumber(256);
            MimeType mimeType = MimeTypeUtils.parseMimeType("application/avro");
            Message<?> inputMessage = MessageBuilder.withPayload(user1).setHeader(MessageHeaders.CONTENT_TYPE,mimeType).build();
            inputDestination.send(inputMessage, "myInput");
            //inputDestination.send(inputMessage, "reverse-in-0");

            Message<byte[]> outputMessage = outputDestination.receive(0, "myInput_");
            Object user2 =  messageConverter.fromMessage(outputMessage, User.class);
            System.out.println(((User)user2).getFavoriteNumber());
            //assertThat(outputMessage.getPayload()).isEqualTo("Hello".getBytes());

            //outputMessage = outputDestination.receive(0, "reverse-out-0");
            //assertThat(outputMessage.getPayload()).isEqualTo("Hello".getBytes());
        }
    }
    //@Test
    public void testMultipleFunctions() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        SampleFunctionConfiguration.class))
                .run(
                        "--spring.cloud.function.definition=uppercase;reverse",
                        "--spring.cloud.stream.bindings.uppercase-in-0.destination=myInput"
                )) {

            InputDestination inputDestination = context.getBean(InputDestination.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> inputMessage = MessageBuilder.withPayload("Hello".getBytes()).build();
            inputDestination.send(inputMessage, "myInput");
            inputDestination.send(inputMessage, "reverse-in-0");

            Message<byte[]> outputMessage = outputDestination.receive(0, "uppercase-out-0");
            assertThat(outputMessage.getPayload()).isEqualTo("Hello".getBytes());

            outputMessage = outputDestination.receive(0, "reverse-out-0");
            assertThat(outputMessage.getPayload()).isEqualTo("Hello".getBytes());
        }
    }

    @SpringBootApplication
    //@Import(TestChannelBinderConfiguration.class)
    public static class SampleConfiguration {



    }
}
