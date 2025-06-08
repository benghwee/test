//package com.himanshu.kafkabinder;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.CountDownLatch;
//
//@Component
//public class KafkaConsumer {
//    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
//
//    private CountDownLatch latch = new CountDownLatch(1);
//    private byte[] payload;
//
//    @KafkaListener(topics = "${test.topic}")
//    public void receive(ConsumerRecord<?, ?> consumerRecord) {
//        LOGGER.info("received payload='{}'", consumerRecord.toString());
//        payload = (byte[]) consumerRecord.value();
//        latch.countDown();
//    }
//
//    public void resetLatch() {
//        latch = new CountDownLatch(1);
//    }
//
//    public CountDownLatch getLatch() {
//        return latch;
//    }
//
//    public void setLatch(CountDownLatch latch) {
//        this.latch = latch;
//    }
//
//    public byte[] getPayload() {
//        return payload;
//    }
//
//    public void setPayload(byte[] payload) {
//        this.payload = payload;
//    }
//}
