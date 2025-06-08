package com.himanshu.kafkabinder;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.function.context.config.SmartCompositeMessageConverter;
import org.springframework.cloud.function.context.converter.avro.AvroSchemaMessageConverter;
import org.springframework.cloud.function.context.converter.avro.AvroSchemaServiceManager;
import org.springframework.cloud.stream.annotation.StreamMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;

import java.util.Collection;
import java.util.function.Function;

@EnableAutoConfiguration
@Configuration
public class SampleFunctionConfiguration {
    @Bean
    public Function<Object, Object> uppercase() {
        return value -> value;
    }

    @Bean
    public Function<Object, Object> reverse() {
        return value -> value;
    }

    @Bean
    public Function<Object, Object> reverse1() {
        return value -> value;
    }

}
