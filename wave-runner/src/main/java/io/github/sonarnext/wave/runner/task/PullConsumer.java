package io.github.sonarnext.wave.runner.task;

import io.github.sonarnext.wave.common.Task;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class PullConsumer {

    private final String URL = System.getProperty("http://localhost:8080/wave/task");
    private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
            0L, java.util.concurrent.TimeUnit.MILLISECONDS,
            new java.util.concurrent.LinkedBlockingQueue<>(100));

    public void start() {
        scheduler.scheduleAtFixedRate(this::pull, 10, 1, java.util.concurrent.TimeUnit.SECONDS);
    }

    public void pull() {

        RestTemplate restTemplate = new RestTemplate();
        Task task = restTemplate.getForObject(URL, Task.class);

        if (task == null) {
            return;
        }
        TaskExecutor taskExecutor = new TaskExecutor();
        executor.execute(taskExecutor);

    }


}
