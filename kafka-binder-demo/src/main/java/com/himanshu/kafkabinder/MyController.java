package com.himanshu.kafkabinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class MyController {

	@Autowired
	StreamBridge streamBridge;
	static int counter = 0;
	@GetMapping("/send")
	public ResponseEntity<HttpStatus> deleteAllTutorials() {

		try {
			if(counter > 9) counter = 0;
			System.out.println("Counter : " + counter);
			System.out.println(Math.abs(Objects.toString(counter,"").hashCode()%9));
			Message<String> message =
					MessageBuilder.withPayload("new data").setHeader("partitionKey", counter).build();

			streamBridge.send("producerBinding-out-0", message);
			counter++;
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
