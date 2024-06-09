package io.clients.feign.comment;

import static java.util.concurrent.TimeUnit.SECONDS;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomRetryer implements Retryer{
	
	private int retryMaxAttempt;

    private long retryInterval;

    private int attempt = 1;
    
    private final int DEFAULT_RETRY = 3;
    
    private final long DEFAULT_INTERVAL = SECONDS.toMillis(5);


    public CustomRetryer(int retryMaxAttempt, Long retryInterval) {
        this.retryMaxAttempt = retryMaxAttempt <= 0 ? DEFAULT_RETRY : retryMaxAttempt;
        this.retryInterval = retryInterval <= 0 ? DEFAULT_INTERVAL : retryInterval;
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        log.info("Feign retry attempt {} due to {} ",  attempt , e.getMessage());

        if(attempt++ == retryMaxAttempt){
            throw e;
        }
        try {
            Thread.sleep(retryInterval);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Retryer clone() {
        return new CustomRetryer(retryMaxAttempt, retryInterval);
    }
}
