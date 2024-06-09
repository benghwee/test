package io.clients.feign.comment;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.Util;
import feign.codec.ErrorDecoder;

public class CustomDecoder implements ErrorDecoder{

	@Override
	public Exception decode(String methodKey, Response response) {
		byte[] body = {};
		try {
			if (response.body() != null) {
				body = Util.toByteArray(response.body().asInputStream());
			}
		} catch (IOException ignored) {
			// defensive catch
		}
		FeignException exception = new FeignException.BadRequest(response.reason(), response.request(), body,
				response.headers());
		if (response.status() >= 400) {
			return new RetryableException(response.status(), response.reason(), response.request().httpMethod(),
					exception, Date.from(Instant.now().plus(15, ChronoUnit.MILLIS)), response.request());
		}
		return exception;
	}

}
