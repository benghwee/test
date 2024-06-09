package io.clients.feign.comment;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.Contract;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.Retryer;
import feign.Util;
import feign.codec.ErrorDecoder;

@Configuration
public class FooConfiguration {

	// @Bean
	// public Retryer retryer() {
	// return new Retryer.Default(SECONDS.toMillis(2), SECONDS.toMillis(10), 5);
	// }

	@Bean
	public Retryer retryer() {
		return new CustomRetryer(5, SECONDS.toMillis(3));
	}

	@Bean
	public ErrorDecoder errorDecoder() {
		return new CustomDecoder();
	}
}
