package io.github.sonarnext.wave.runner.task;

import io.github.sonarnext.wave.common.Task;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class PullConsumer {

    private final String URL = System.getProperty("http://localhost:8080/wave/task");
    private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

    public void start(TaskExecutor taskExecutor) {
        scheduler.scheduleAtFixedRate(taskExecutor, 10, 1, java.util.concurrent.TimeUnit.SECONDS);
    }

    public void pull() {

        RestTemplate restTemplate = new RestTemplate();
        Task task = restTemplate.getForObject(URL, Task.class);

    }


}
