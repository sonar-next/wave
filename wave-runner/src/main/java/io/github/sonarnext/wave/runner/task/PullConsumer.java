package io.github.sonarnext.wave.runner.task;

import io.github.sonar.next.wave.EnumProto;
import io.github.sonar.next.wave.PollTaskRequest;
import io.github.sonar.next.wave.PollTaskResponse;
import io.github.sonarnext.wave.common.connection.ConnectionServiceServer;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

public class PullConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PullConsumer.class);

    private volatile boolean running = true;

    private final String URL = System.getProperty("SERVER_URL", "http://127.0.0.1:8080") + "/wave/task";
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
            0L, java.util.concurrent.TimeUnit.MILLISECONDS,
            new java.util.concurrent.LinkedBlockingQueue<>(100));

    public void start() {
        do {
            this.pull();
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(1000));
        } while (running);
    }

    public void pull() {

        try {
            RestTemplate restTemplate = new RestTemplateBuilder().build();
            EnumProto.Task task = restTemplate.getForObject(URL, EnumProto.Task.class);

            if (task == null) {
                logger.debug("PullConsumer pull task is null");
                return;
            }
            TaskExecutor taskExecutor = new TaskExecutor();
            executor.execute(taskExecutor);
        } catch (RestClientException e) {
            logger.warn("PullConsumer pull error, reason = {}", e.getMessage());
        }

    }

    public void end() {
        scheduler.shutdown();
        executor.shutdown();
    }

}
