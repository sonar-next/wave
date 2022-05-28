package io.github.sonarnext.wave.server.task;


import java.util.concurrent.*;

/**
 * manage tasks
 */
public class TaskManager {

    private static final BlockingQueue<TaskEntity> TASK_ENTITY_LIST = new LinkedBlockingQueue<>(10000);

    static TaskEntity getTask() throws InterruptedException {
        return TASK_ENTITY_LIST.poll(10, TimeUnit.SECONDS);
    }

    static boolean addTask(TaskEntity taskEntity) {

        return TASK_ENTITY_LIST.offer(taskEntity);
    }

}
