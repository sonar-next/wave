package io.github.sonarnext.wave.server.task;

import io.github.sonarnext.wave.common.Task;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/wave/task")
@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Task pullAndGetTask() {
        return taskService.pullAndGetTask();
    }


}
