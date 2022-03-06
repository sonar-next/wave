package io.github.sonarnext.wave.server.task;

import io.github.sonarnext.wave.common.Task;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {


    @Override
    public Task pullAndGetTask() {
        return TaskManager.getTask();
    }

}
