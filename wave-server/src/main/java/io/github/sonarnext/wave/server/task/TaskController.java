package io.github.sonarnext.wave.server.task;

import io.github.sonar.next.wave.EnumProto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public EnumProto.Task pullAndGetTask() {
        return this.taskService.pullAndGetTask();
    }

    @PostMapping
    public String addTask(@Validated EnumProto.Task task) {
        return this.taskService.addTask(task);
    }


}
