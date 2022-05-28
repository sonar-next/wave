package io.github.sonarnext.wave.runner.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 任务启动
 */
@Component
public class TaskLauncher implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(TaskLauncher.class);

    @Override
    public void run(ApplicationArguments args) {
        logger.info("TaskLauncher start");

        PullConsumer pullConsumer = new PullConsumer();
        pullConsumer.start();

        logger.info("TaskLauncher end");
    }
}
