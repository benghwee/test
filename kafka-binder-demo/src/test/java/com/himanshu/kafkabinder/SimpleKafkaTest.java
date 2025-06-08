package com.himanshu.kafkabinder;

import example.avro.User;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.lang.management.ThreadInfo;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.kafka.support.KafkaHeaders.TOPIC;


@SpringBootTest(classes = SimpleKafkaTest.SampleConfiguration.class)
@ActiveProfiles("test")
//@EmbeddedKafka
//@EmbeddedKafka(partitions = 1, topics = { "testTopic" })
public class SimpleKafkaTest {
    private static final String TEST_TOPIC = "testTopic";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    @Autowired
    static EmbeddedKafkaBroker embeddedKafkaBroker;
    //@Autowired
    //LiquibaseRunner lq;


    @BeforeEach
    public void setUp() throws InterruptedException {
        embeddedKafkaBroker = new EmbeddedKafkaBroker(1, true, TOPIC);
        embeddedKafkaBroker.kafkaPorts(9092);
        embeddedKafkaBroker.afterPropertiesSet();
        embeddedKafkaBroker.addTopics(TEST_TOPIC);
//
//        Thread.sleep(10000);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        // Clean up resources or perform teardown actions here
        Thread.sleep(1000);
        //embeddedKafkaBroker.destroy();
        System.out.println("Kafka Broker shutdown...");
    }

    @Test
    public void testReceivingKafkaEvents() throws InterruptedException {
        Consumer<String, SpecificRecord> consumer = configureConsumer();
        Producer<String, SpecificRecord> producer = configureProducer();
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);

        KafkaDeleteRecords deleteRecords = new KafkaDeleteRecords();
        //deleteRecords.deleteRecord(TEST_TOPIC);
        ProducerRecord<String, SpecificRecord> record = new ProducerRecord<>(TEST_TOPIC, "123", user1);
        //record.headers().
        producer.send(record);
        Thread.sleep(5000);
        ConsumerRecord<String, SpecificRecord> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TEST_TOPIC);

        assertThat(singleRecord).isNotNull();
        //assertThat(singleRecord.key()).isEqualTo(123);
        assertThat(((User)singleRecord.value()).getFavoriteNumber()).isEqualTo(256);
        Object o = singleRecord.value();
        System.out.println(o.getClass());
        consumer.close();
        producer.close();
    }


    private Consumer<String, SpecificRecord> configureConsumer() {
        //Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        //consumerProps.put("bootstrap.servers","localhost:9092");
        //Properties consumerProps = new Properties();
        Map<String, Object> consumerProps = new HashMap<>();
        //consumerProps.put("bootstrap.servers","localhost:9092");
        consumerProps.put("bootstrap.servers","localhost:9092");
        consumerProps.put("auto.create.topics.enable","true");
        //consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleAvroConsumer");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        consumerProps.put(SCHEMA_REGISTRY_URL_CONFIG, "mock://localhost.something");
        consumerProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
        Consumer<String, SpecificRecord> consumer = new DefaultKafkaConsumerFactory<String, SpecificRecord>(consumerProps)
                .createConsumer();
        //Consumer<String, SpecificRecord> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singleton(TEST_TOPIC));
        return consumer;
    }

    private Producer<String, SpecificRecord> configureProducer() {
        //Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put("bootstrap.servers","localhost:9092");
        producerProps.put("auto.create.topics.enable","true");
        //Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, "AvroProducer");
        //producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class.getName());
        producerProps.put(VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        producerProps.put(SCHEMA_REGISTRY_URL_CONFIG, "mock://localhost.something");
        producerProps.put(AbstractKafkaAvroSerDeConfig.AUTO_REGISTER_SCHEMAS, true);
        //return new KafkaProducer<>(producerProps);
        return new DefaultKafkaProducerFactory<String, SpecificRecord>(producerProps).createProducer();
    }

    @SpringBootApplication
    //@Import(TestChannelBinderConfiguration.class)
    public static class SampleConfiguration {



    }
}
