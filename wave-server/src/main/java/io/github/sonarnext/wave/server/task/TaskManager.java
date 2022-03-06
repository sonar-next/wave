package io.github.sonarnext.wave.server.task;


import io.github.sonarnext.wave.common.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * manage tasks
 */
public class TaskManager {

    private static final BlockingDeque<Task> taskList = new LinkedBlockingDeque<>();

    static Task getTask() {
        return taskList.poll();
    }

}
