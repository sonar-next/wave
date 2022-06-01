package io.github.sonarnext.wave.server.task;


import io.github.sonar.next.wave.EnumProto;

import java.util.concurrent.*;

/**
 * manage tasks
 */
public class TaskManager {

    private static final BlockingQueue<EnumProto.Task> TASK_ENTITY_LIST = new LinkedBlockingQueue<>(10000);

    static EnumProto.Task getTask() throws InterruptedException {
        return TASK_ENTITY_LIST.poll(10, TimeUnit.SECONDS);
    }

    public static boolean addTask(EnumProto.Task task) {
        return TASK_ENTITY_LIST.offer(task);
    }

}
