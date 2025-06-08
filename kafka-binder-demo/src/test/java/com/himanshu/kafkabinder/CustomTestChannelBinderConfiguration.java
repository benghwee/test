package com.himanshu.kafkabinder;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.function.context.converter.avro.AvroSchemaMessageConverter;
import org.springframework.cloud.function.context.converter.avro.AvroSchemaServiceManager;
import org.springframework.cloud.stream.annotation.StreamMessageConverter;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomTestChannelBinderConfiguration extends TestChannelBinderConfiguration {
//    @Bean
//    //@ConditionalOnMissingBean
//    public AvroSchemaMessageConverter messageConverter(AvroSchemaServiceManager manager){
//        return new AvroSchemaMessageConverter(manager);
//    }
}
