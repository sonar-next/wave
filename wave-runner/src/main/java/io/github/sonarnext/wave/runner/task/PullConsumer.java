package io.github.sonarnext.wave.runner.task;

import io.github.sonar.next.wave.EnumProto;
import io.github.sonar.next.wave.PollTaskRequest;
import io.github.sonar.next.wave.PollTaskResponse;
import io.github.sonarnext.wave.common.connection.ConnectionServiceServer;
import io.github.sonarnext.wave.common.vo.Pager;
import io.github.sonarnext.wave.common.vo.TaskVO;
import io.github.sonarnext.wave.runner.config.RestClientConfiguration;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;
import java.util.List;

public class PullConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PullConsumer.class);

    private volatile boolean running = true;

    private final String URL = System.getProperty("SERVER_URL", "http://127.0.0.1:8080") + "/wave/task/pull" +
            "";
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
            0L, java.util.concurrent.TimeUnit.MILLISECONDS,
            new java.util.concurrent.LinkedBlockingQueue<>(100));

    private final RestTemplate restTemplate = RestClientConfiguration.restTemplate();

    public void start() {
        do {
            this.pull();
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(1000));
        } while (running);
    }

    public void pull() {

        try {
            Pager<TaskVO> task = restTemplate.postForObject(URL, "", Pager.class);

            if (task == null || CollectionUtils.isEmpty(task.getData())) {
                logger.debug("PullConsumer pull task is null");
                return;
            }
            logger.info("receive task {}", task.getData());
            for (TaskVO taskVO : task.getData()) {
                TaskExecutor taskExecutor = new TaskExecutor();
                taskExecutor.init(taskVO);
                executor.execute(taskExecutor);
            }

        } catch (RestClientException e) {
            logger.warn("PullConsumer pull error, reason = {}", e.getMessage());
        }

    }

    public void end() {
        scheduler.shutdown();
        executor.shutdown();
    }

}
