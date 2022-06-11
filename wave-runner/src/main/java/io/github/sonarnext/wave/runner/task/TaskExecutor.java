package io.github.sonarnext.wave.runner.task;


import io.github.sonarnext.wave.common.vo.TaskVO;
import io.github.sonarnext.wave.runner.sonar.SonarProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * run
 */
public class TaskExecutor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TaskExecutor.class);

    private TaskVO task;
    /**
     * 1. receive task
     * 2. convert task to taskExecutor
     * 3. execute task
     */
    @Override
    public void run() {
        logger.info("start process task {}", task);
        SonarProcessor sonarProcessor = new SonarProcessor();
        try {
            sonarProcessor.process(task);
        } catch (IOException | InterruptedException e) {
            logger.warn("e ", e);
        }

    }

    public void init(TaskVO data) {
        this.task = data;
    }
}
